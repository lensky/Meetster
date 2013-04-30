(ns meetster-server.utils)

(defmacro as-results-of [fun vars & body]
  `(let [~vars (map ~fun ~vars)]
     ~@body))
