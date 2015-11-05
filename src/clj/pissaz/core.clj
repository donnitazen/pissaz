(ns pissaz.core
  (:require
    [org.httpkit.server :as http]
    [noir.cookies :as cookies]
    [noir.session :as session]
    [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
    [pissaz.routes :refer [all-routes]]
    [com.stuartsierra.component :as component]))

(defrecord Server [routes port]
  component/Lifecycle
  (start [this]
    (let [app (wrap-defaults (:tabel routes) site-defaults)]
      (assoc this
        :stop-fn
        (http/run-server app {:port port}))))
  (stop [this]
    ((:stop-fn this))
    (dissoc this :stop-fn)))

(defn create [port]
  (-> (map->Server {:port port})
      (component/using [:routes])))

(defn -main
  "Main entry to the application"
  [& args]
  (start)
  (println "Pissaz is up and running well!"))


;;



