(ns pissaz.dbase
  (:require
    [com.stuartsierra.component :as component]))

(defrecord Database [quiz question user]
  component/Lifecycle
  (start [this]
    this)
  (stop [this]
    this))

(defn create [quiz question user]
  (map->Database {:question-db (slurping-file question)
                  :quiz-db (slurping-file quiz)
                  :user-db (slurping-file user)}))


(defn slurping-file
  [filename]
  (slurp (str "resources/data/" filename)))