(ns meetster-server.sql
  (:require [clojure.java.jdbc :as sql])
  (:require [clojureql.core :as cql])
  (:use [meetster-server.utils :only [as-results-of]]))

(defn epoch->sql-timestamp [epoch-string]
  (new java.sql.Timestamp epoch-string))

(defmacro as-sql-timestamp [vars & body]
  `(as-results-of
    epoch->sql-timestamp
    ~vars
    ~@body))

(defmacro with-connection [& body]
  `(sql/with-connection database-uri ~@body))

(def database-uri (or (System/getenv "HEROKU_POSTGRESQL_PINK_URL")
                      "postgresql://localhost:5432/meetster-server"))

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

(defn insert-user [user-info]
  (let [{:keys [email first_name last_name]}
        user-info]
    (sql/insert-record
     :users
     {:email email :first_name first_name :last_name last_name})))

(defn get-user-by-email [email]
  (cql/with-results [rs (cql/select (cql/table :users)
                                    (cql/where (= :email email)))]
    (first rs)))

(defn get-user-by-id [id]
  (cql/with-results [rs (cql/select (cql/table :users)
                                    (cql/where (= :id id)))]
    (first rs)))

(defn insert-event [event-info]
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

(defn get-new-events [userid last-sync-time]
  (sql/with-query-results rs
    ["select events.* from (events inner join (select eventid from invitees where inviteeid=?) as eventid on events.id = eventid) where events.creation_time > ?"
     userid last-sync-time]
    (doall rs)))


(defn search-user-by-email [email-part]
  (sql/with-query-results rs
    ["SELECT users.* from users where (users.email ILIKE ?)"
     (str "%" email-part "%")]
    (doall rs)))

(defn get-users [userids]
  (cql/with-results
    [rs
     (cql/select (cql/table :users)
                 (cql/where (in :users.id userids)))]
    (doall rs)))
