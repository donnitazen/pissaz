(ns pissaz.core
  (:require
    [org.httpkit.server :as http]
    [clojure.string :as cs]
    [noir.cookies :as cookies]
    [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
    [pissaz.routes :refer [all-routes]]))



(defonce server (atom nil))

(defn start
  ([] (start 3000))
  ([port] (reset! server
                  (-> all-routes
                      (cookies/wrap-noir-cookies*)
                      (wrap-defaults (update-in site-defaults
                                                [:security :anti-forgery]
                                                #(not %)))
                      (http/run-server {:port port})))))

(defn stop
  []
  (@server)
  (reset! server nil))

(defn reset
  []
  (stop)
  (start))



