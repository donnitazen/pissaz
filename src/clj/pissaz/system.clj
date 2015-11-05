(ns pissaz.system
  (:require
    [com.stuartsierra.component :as component]
    [pissaz.dbase :as db]
    [pissaz.routes :as routes]
    [pissaz.core :as server]
    [clojure.tools.namespace.repl :refer [refresh]]))

(defn create-system
  [config-file]
  (let [{:keys [port db public-directory]}
        (->> config-file slurp read-string)
        {:keys [question-db quiz-db user-db]} db]           ;ini db yang diambil dr config
    (component/system-map
      :database (db/create quiz-db question-db user-db)
      :routes (routes/create public-directory)
      :server (server/create port))))

(def conf "resources/config.edn")

(defonce system (create-system conf))

(defn init
  []
  (alter-var-root
    #'system
    (constantly (create-system conf))))

(defn start
  []
  (alter-var-root
    #'system
    component/start))

(defn stop
  []
  (alter-var-root
    #'system
    (fn [s] (component/stop s))))

(defn go
  []
  (init)
  (start) system)

(defn reset
  []
  (stop)
  (refresh :after 'pissaz.system/go))


