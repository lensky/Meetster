(defproject meetster-server "0.1.0"
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [ring/ring-core "1.1.8"]
                 [ring/ring-jetty-adapter "1.1.8"]
                 [ring/ring-json "0.2.0"]
                 [cheshire "5.1.1"]
                 [org.clojure/java.jdbc "0.2.3"]
                 [postgresql "9.1-901.jdbc4"]
                 [clojureql "1.0.4"]]
  :plugins [[lein-ring "0.8.5"]]
  :ring {:handler meetster-server.core/app}
  :min-lein-version "2.0.0")
