(ns pissaz.users)


(defn add-user
  [data user-database]
  (let [current-user-id (str (inc (count user-database)))
        new-user (assoc data :user-id current-user-id)]
    (do (spit "resources/data/user.edn" (conj user-database new-user))
        (new-user :username))))


(defn registered?
  [username password user-database]
  (let [username-check #(get % :username)
        password-check #(get % :password)]
    (first (filter #(and (= username (username-check %))
                   (= password (password-check %)))
             user-database))))

(defn all-data-filled?
  [a-map]
  (every? #(not= % "") (vals a-map)))

(defn username-and-email-new?
  [a-map user-database]
  (let [usernames (map #(% :username) user-database)
        emails (map #(% :email) user-database)]
    (and (not-any? #(= % (a-map :username)) usernames)
        (not-any? #(= % (a-map :email)) emails))))

(defn password-matched?
  [a-map]
  (= (a-map :password) (a-map :password-confirmation)))

(defn sign-up-validated?
  [a-map user-database]
  (and (all-data-filled? a-map)
       (username-and-email-new? a-map user-database)
       (password-matched? a-map)))

(defn admin?
  [username user-database]
  (->> (filter #(= username (% :username)) user-database)
       (first)
       (#(= (% :role) "admin"))))

