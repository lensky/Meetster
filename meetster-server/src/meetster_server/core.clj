(ns meetster-server.core
  (:use [ring.adapter.jetty :only [run-jetty]])
  (:use [ring.middleware.params :only [wrap-params]])
  (:require [cheshire.core :as json])
  (:use [cheshire.generate :only [add-encoder]])
  (:require [meetster-server.sql :as sql])
  (:import [java.util.Date]))

;; For api consistency
(def ^:dynamic *params-userinfo* "userinfo")
(def ^:dynamic *params-userid* "userid")
(def ^:dynamic *params-lastsynctime* "last-sync-time")
(def ^:dynamic *params-events* "events")
(def ^:dynamic *params-email* "email")
(def ^:dynamic *params-userids* "userids")

;; JSON setup
(add-encoder java.util.Date
             (fn [d jsonGenerator]
               (.writeRaw jsonGenerator ":")
               (.writeRaw jsonGenerator (String/valueOf (.getTime d)))))
(add-encoder java.sql.Timestamp
             (fn [d jsonGenerator]
               (.writeRaw jsonGenerator ":")
               (.writeRaw jsonGenerator (String/valueOf (.getTime d)))))

(defn make-user [req]
  (let [new-user-info (json/parse-string
                       (get (:params req) *params-userinfo*)
                       true)]
    (let [id (:id (sql/with-connection (sql/insert-user new-user-info)))]
      {:status 200
       :headers {"Content-Type" "application/json"}
       :body (json/generate-string {:id id})})))

(defn sync-user [req]
  (let [userid (Integer/parseInt (get (:params req) *params-userid*))
        last-sync-time (sql/epoch->sql-timestamp
                        (Long/parseLong (get (:params req) *params-lastsynctime*)))
        remote-new-events (json/parse-string (get (:params req) *params-events*) true)]
    (let [local-new-events
          (sql/with-connection
            (dorun (map #(sql/insert-event %) remote-new-events))
            (sql/get-new-events userid last-sync-time))]
      {:status 200
       :headers {"Content-Type" "application/json"}
       :body (json/generate-string local-new-events)})))

(defn test-post [req]
  {:status 200
   :headers {"Content-Type" "text/plain"}
   :body (pr-str req)})

(defn get-user-by-email [req]
  (let [email (get (:params req) *params-email*)
        local-user (sql/with-connection (sql/get-user-by-email email))]
    (if (nil? local-user)
      {:status 404
       :headers {"Content-Type" "text/html"}
       :body "User not found."}
      {:status 200
       :headers {"Content-Type" "application/json"}
       :body (json/generate-string local-user)})))

(defn search-user-by-email [req]
  (let [email (get (:params req) *params-email*)
        local-users (sql/with-connection (sql/search-user-by-email email))]
    {:status 200
     :headers {"Content-Type" "application/json"}
     :body (json/generate-string local-users)}))


(defn get-users [req]
  (let [userids (json/parse-string (get (:params req) *params-userids*))
        users (sql/with-connection (sql/get-users userids))]
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
