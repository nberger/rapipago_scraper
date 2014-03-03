(ns rapipago-search.core
  (use [net.cgrand.enlive-html :as html])
  (require [clj-http.client :as http]))

(defn fetch-home
  "Fetches the home page for RapiPago"
  []
  (html/html-resource
    (java.io.StringReader.
      (:body (http/get "http://www.rapipago.com.ar/rapipagoWeb/index.htm")))))

(defn province-options
  []
  (html/select (fetch-home) [:select#provinciaSuc :option]))

(defn is-option-empty?
  [option]
  (let [{{value :value} :attrs} option]
    (empty? value)))

(defn build-province-from-option
  [option]
  (let [{content :content {value :value} :attrs} option]
    {:name (first content) :id value}))

(comment
  (build-province-from-option {:content '("Buenos Aires") :attrs {:value "1"}})
)

(defn provinces
  "List of provinces where RapiPago has shops"
  []
  (map build-province-from-option
       (filter (comp not is-option-empty?) (province-options))))
