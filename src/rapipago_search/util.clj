(ns rapipago-search.util)

(defn is-option-empty?
  [option]
  (let [{{value :value} :attrs} option]
    (empty? value)))

(def is-option-present? (complement is-option-empty?))

(defn parse-option
  [option]
  (let [{content :content {value :value} :attrs} option]
    {:name (first content) :id value}))

(comment
  (parse-option {:content '("Buenos Aires") :attrs {:value "1"}})
)

