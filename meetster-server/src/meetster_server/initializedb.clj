(ns meetster-server.initializedb
  (:require [meetster-server.sql :as sql]))

(defn -main []
  (println "Creating databases...")
  (println sql/database-uri)
  (sql/with-connection (sql/initialize-database))
  (println "Done."))
