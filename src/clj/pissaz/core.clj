(ns pissaz.core
  (:require
    [org.httpkit.server :as http]
    [clojure.string :as cs]
    [ring.middleware.defaults :as defaults]
    [pissaz.routes :refer [all-routes]]))

(def app
  (-> all-routes
      (defaults/wrap-defaults defaults/site-defaults)))

(defonce server (atom nil))

(defn start
  []
  (reset! server (http/run-server app {:port 3000})))

(defn stop
  []
  (@server)
  (reset! server nil))

(defn reset
  []
  (stop)
  (start))

