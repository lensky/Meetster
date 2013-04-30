(ns meetster-server.core
  (:use [ring.adapter.jetty :only [run-jetty]])
  (:use [ring.middleware.params :only [wrap-params]])
  (:require [clojure.java.jdbc :as sql])
  (:require [clojureql.core :as cql])
  (:require [cheshire.core :as json])
  (:import [java.util.Date]))

(defmacro as-results-of [fun vars & body]
  `(let [~vars (map ~fun ~vars)]
     ~@body))

(defmacro as-sql-timestamp [vars & body]
  `(as-results-of
    (fn [var] (new java.sql.Timestamp (.getTime (sql-string-to-date var))))
    ~vars
    ~@body))

(def database-uri (or (System/getenv "HEROKU_POSTGRESQL_BLACK_URL")
                      "postgresql://localhost:5432/meetster-server"))

(def timestamp-formatter (new java.text.SimpleDateFormat "yyyy-MM-dd HH:mm"))

(defn sql-string-to-date [date-string]
  (.parse timestamp-formatter date-string))

(defn sql-date-to-string [date]
  (.format timestamp-formatter date))

;; Route info (useful for API consistency)
(def ^:dynamic *params-userinfo* "userinfo")
(def ^:dynamic *params-userid* "userid")
(def ^:dynamic *params-lastsynctime* "last-sync-time")
(def ^:dynamic *params-events* "events")
(def ^:dynamic *params-email* "email")
(def ^:dynamic *params-userids* "userids")

;; Code to initialize the table
;; ----------------------------
(defn create-users-table []
  (sql/create-table
   :users
   [:id :serial "PRIMARY KEY"]
   [:first_name :text]
   [:last_name :text]
   [:email :text :unique]))

(defn create-categories-table []
  (sql/create-table
   :categories
   [:id :serial "PRIMARY KEY"]
   [:description :text])
  (dorun
   (map #(sql/insert-record :categories {:description %})
        ["Sports" "Food" "Entertainment" "Work" "Miscellaneous"])))

(defn create-events-table []
  (sql/create-table
   :events
   [:id :serial "PRIMARY KEY"]
   [:creatorid :integer "REFERENCES users (id)"]
   [:creation_time :timestamp "DEFAULT CURRENT_TIMESTAMP"]
   [:categoryid :integer "REFERENCES categories (id)"]
   [:description :text]
   [:start_time :timestamp]
   [:end_time :timestamp]
   [:latitude :real]
   [:longitude :real]
   [:max_radius :real]
   [:location_description :text]))

(defn create-invitees-table []
  (sql/create-table
   :invitees
   [:eventid :integer "REFERENCES events (id)"]
   [:inviteeid :integer "REFERENCES users (id)"]
   ["PRIMARY KEY (eventid, inviteeid)"]))

(defn initialize-database []
  (create-users-table)
  (create-categories-table)
  (create-events-table)
  (create-invitees-table))
;; ----------------------------

(defn sql-insert-user [user-info]
  (let [{:keys [email first_name last_name]}
        user-info]
    (sql/insert-record
     :users
     {:email email :first_name first_name :last_name last_name})))

(defn sql-get-user-by-email [email]
  (cql/with-results [rs (cql/select (cql/table :users)
                                    (cql/where (= :email email)))]
    (first rs)))

(defn sql-get-user-by-id [id]
  (cql/with-results [rs (cql/select (cql/table :users)
                                    (cql/where (= :id id)))]
    (first rs)))

(defn make-user [req]
  (let [new-user-info (json/parse-string
                       (get (:params req) *params-userinfo*)
                       true)]
    (let [id (:id (sql/with-connection database-uri (sql-insert-user new-user-info)))]
      {:status 200
       :headers {"Content-Type" "application/json"}
       :body (json/generate-string {:id id})})))

(defn sql-insert-event [event-info]
  (let [{:keys [creatorid categoryid description start_time end_time
         latitude longitude max_radius location_description invitee_ids_string]}
        event-info]
    (as-sql-timestamp [start_time end_time]
     (let [event-id
           (:id (sql/insert-record
                 :events
                 {:creatorid creatorid :categoryid categoryid :description description
                  :start_time start_time :end_time end_time :latitude latitude :longitude longitude
                  :max_radius max_radius :location_description location_description}))
           invitee-ids (if (nil? invitee_ids_string)
                         '()
                         (map #(Integer/parseInt %) (clojure.string/split invitee_ids_string #",")))]
       (dorun
        (for [invitee-id invitee-ids]
          (sql/insert-record
           :invitees
           {:eventid event-id :inviteeid invitee-id})))))))

(defn sql-get-new-events [userid last-sync-time]
  (sql/with-query-results rs
    [(format "select events.* from (events inner join (select eventid from invitees where inviteeid=?) as eventid on events.id = eventid) where events.creation_time > '%s'" last-sync-time)
     userid]
    (doall rs)))

(defn sync-user [req]
  (let [userid (Integer/parseInt (get (:params req) *params-userid*))
        last-sync-time (get (:params req) *params-lastsynctime*)
        remote-new-events (json/parse-string (get (:params req) *params-events*) true)]
    (let [local-new-events
          (sql/with-connection database-uri
            (dorun (map #(sql-insert-event %) remote-new-events))
            (sql-get-new-events userid last-sync-time))]
      {:status 200
       :headers {"Content-Type" "application/json"}
       :body (json/generate-string local-new-events {:date-format "yyyy-MM-dd HH:mm"})})))

(defn test-post [req]
  {:status 200
   :headers {"Content-Type" "text/plain"}
   :body (pr-str req)})

(defn get-user-by-email [req]
  (let [email (get (:params req) *params-email*)
        local-user (sql/with-connection database-uri (sql-get-user-by-email email))]
    (if (nil? local-user)
      {:status 404
       :headers {"Content-Type" "text/html"}
       :body "User not found."}
      {:status 200
       :headers {"Content-Type" "application/json"}
       :body (json/generate-string local-user)})))

(defn sql-search-user-by-email [email-part]
  (sql/with-query-results rs
    ["SELECT users.* from users where (users.email ILIKE ?)"
     (str "%" email-part "%")]
    (doall rs)))

(defn search-user-by-email [req]
  (let [email (get (:params req) *params-email*)
        local-users (sql/with-connection database-uri (sql-search-user-by-email email))]
    {:status 200
     :headers {"Content-Type" "application/json"}
     :body (json/generate-string local-users)}))

(defn sql-get-users [userids]
  (cql/with-results
    [rs
     (cql/select (cql/table :users)
                 (cql/where (in :users.id [1 2 3 4])))]
    (doall rs)))

(defn get-users [req]
  (let [userids (json/parse-string (get (:params req) *params-userids*))
        users (sql-get-users userids)]
    {:status 200
     :headers {"Content-Type" "application/json"}
     :body (json/generate-string users)}))

(defn app [req]
  ((condp = (:uri req)
     "/make-user" (wrap-params make-user)
     "/get-user-by-email" (wrap-params get-user-by-email)
     "/sync-user" (wrap-params sync-user)
     "/test-post" (wrap-params test-post)
     "/search-user-by-email" (wrap-params search-user-by-email)
     "/get-users" (wrap-params get-users)
     "/" (fn [req] {:status 200
                    :headers {"Content-Type" "text/html"}
                    :body "Hello, Dave."}))
   req))

(defn -main [port]
  (run-jetty app {:port (Integer. port)}))
