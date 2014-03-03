(ns rapipago-search.core
  (use [net.cgrand.enlive-html :as html])
  (require [clj-http.client :as http]))

(defn- fetch-home
  "Fetches the home page for RapiPago"
  []
  (html/html-resource
    (java.io.StringReader.
      (:body (http/get "http://www.rapipago.com.ar/rapipagoWeb/index.htm")))))

(defn- province-options
  []
  (html/select (fetch-home) [:select#provinciaSuc :option]))

(defn- is-option-empty?
  [option]
  (let [{{value :value} :attrs} option]
    (empty? value)))

(def is-option-present? (complement is-option-empty?))

(defn- parse-option
  [option]
  (let [{content :content {value :value} :attrs} option]
    {:name (first content) :id value}))

(comment
  (parse-option {:content '("Buenos Aires") :attrs {:value "1"}})
)

(defn- fetch-cities-options
  [province]
  (let [url "http://www.rapipago.com.ar/rapipagoWeb/consultar-ciudades.htm"
        province-id (:id province)]
    (:body (http/get url
                     {:query-params {:provinciaId province-id}}))))

(comment
  (fetch-cities-options (second (provinces)))
)

(defn- cities-options
  [province]
  (let [reader (java.io.StringReader. (fetch-cities-options province))
        options-html (html/html-resource reader)]
    (html/select options-html [:option])))

(comment
  (cities-options (second (provinces)))
)

(defn provinces
  "List of provinces where RapiPago has shops"
  []
  (map parse-option
       (filter is-option-present? (province-options))))

(defn cities
  "List of cities in a province"
  [province]
  (map parse-option
       (filter is-option-present? (cities-options province))))

(comment
  (cities (second (provinces)))
)
