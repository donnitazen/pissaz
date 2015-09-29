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

(defn translate-roman-numeral
  [roman-numeral]
  (let [base {\I 1 \V 5 \X 10 \L 50 \C 100 \D 500 \M 1000}
        roman-number (map base roman-numeral)
        maxnum (cond
                 (empty? roman-numeral) 0
                 (= 1 (count roman-numeral)) (first roman-number)
                 :else (apply max roman-number))
        left-side (take-while #(< (base %) maxnum) roman-numeral)
        right-side (rest (drop-while #(< (base %) maxnum) roman-numeral))]
    (cond
      (empty? roman-numeral) 0
      :else (+ (- maxnum (translate-roman-numeral left-side)) (translate-roman-numeral right-side)))
    ))

(defn to-roman-number
  [num]
  (let [jumlah (count (str num))
        pisah (map * (map #(- (int %) 48) (str num))
                      (iterate #(/ % 10) (apply * (repeat jumlah 10))))]
    pisah))

