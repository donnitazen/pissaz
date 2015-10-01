(ns pissaz.quiz)

; Function to read from edn

(defn read-question-file
  [filename]
  (read-string (slurp (str "resources/data/" filename))))


; Function to show the question

(defn show-question
  [question]
  (let [shuffled (map-indexed #(conj %2 %1) (shuffle (question :answers)))
        true-answer (mapv last (filterv second shuffled))]
    {:type (question :type)
     :problem        (:problem question)
     :answer-choices (mapv #(assoc {} :idx (last %) :string (first %)) shuffled)
     :answer-index   true-answer}))


; Function to check the rightful answer

(defn answer-check
  [q-type user-choice-idx answer-index]
  (cond
    (= q-type "sa") (= (read-string user-choice-idx) (first (read-string answer-index)))
    (= q-type "ma") (if (string? user-choice-idx)
                      false
                      (= (mapv read-string user-choice-idx) (read-string answer-index)))))

; Function to add a question

(defn add-question
  [q-map]
  (let [all-questions (read-question-file "question.edn")
        true-answers (mapv #(vector % true) (read-string (q-map :true-answers)))
        false-answers (mapv #(vector % false) (read-string (q-map :false-answers)))
        q-id (str (inc (read-string ((last all-questions) :question-id))))
        new-answers (vec (concat true-answers false-answers))
        question-with-answers (merge q-map {:question-id q-id :answers new-answers :type (if (> 1 (count true-answers)) "ma" "sa")})
        new-question (dissoc question-with-answers :true-answers :false-answers)]
    (spit "resources/data/question.edn" (conj all-questions new-question))
    q-id))

; Function to move to the next question

(defn last-question?
  [q-id]
  (= q-id ((last (read-question-file "question.edn")) :question-id)))
