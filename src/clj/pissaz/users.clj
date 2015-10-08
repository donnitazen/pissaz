(ns pissaz.users)

(defn read-user-file
  []
  (read-string (slurp (str "resources/data/" "user.edn"))))

(defn add-user
  [data]
  (let [current-user-id (str (inc (count (read-user-file))))
        new-user (assoc data :user-id current-user-id)]
    (do (spit "resources/data/user.edn" (conj (read-user-file) new-user))
        (new-user :username))))



