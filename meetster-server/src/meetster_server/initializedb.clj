(ns meetster-server.intializedb
  [:require [meetster-server.sql :as sql]])

(defn -main []
  (println "Creating databases...")
  (sql/with-connection initialize-database)
  (println "Done."))
