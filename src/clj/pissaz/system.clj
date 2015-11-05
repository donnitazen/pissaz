(ns pissaz.system
  (:require
    [com.stuartsierra.component :as component]
    [pissaz.dbase :as db]
    [pissaz.routes :as routes]
    [pissaz.core :as server]
    [clojure.tools.namespace.repl :refer [refresh]]))

(defn create-system
  [config-file]
  (let [{:keys [port db]}
        (->> config-file slurp read-string)
        {:keys [question-db quiz-db user-db]} db]
    (component/system-map
      :database (db/create question-db quiz-db user-db)
      :routes (routes/create)
      :server (server/create port))))

(def conf "resources/config.edn")

(defonce system (create-system conf))


