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


(defn registered?
  [username password]
  (let [username-check #(get % :username)
        password-check #(get % :password)]
    (first (filter #(and (= username (username-check %))
                   (= password (password-check %)))
             (read-user-file)))))

(defn all-data-filled?
  [a-map]
  (every? #(not= % "") (vals a-map)))

(defn username-and-email-new?
  [a-map]
  (let [usernames (map #(% :username) (read-user-file))
        emails (map #(% :email) (read-user-file))]
    (and (not-any? #(= % (a-map :username)) usernames)
        (not-any? #(= % (a-map :email)) emails))))

(defn password-matched?
  [a-map]
  (= (a-map :password) (a-map :password-confirmation)))

(defn sign-up-validated?
  [a-map]
  (and (all-data-filled? a-map)
       (username-and-email-new? a-map)
       (password-matched? a-map)))

(defn admin?
  [username]
  (->> (filter #(= username (% :username)) (read-user-file))
       (first)
       (#(= (% :role) "admin"))))

