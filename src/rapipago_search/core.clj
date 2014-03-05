(ns rapipago-search.core
  (require [rapipago-search.provinces :as provinces]
           [rapipago-search.cities :as cities]
           [net.cgrand.enlive-html :as html]
           [clj-http.client :as http]))

(defn- fetch-search-result
  [{province :province city :city, :as filter}]
  (let [url "http://www.rapipago.com.ar/rapipagoWeb/suc-buscar.htm"
        post-params {:provinciaSuc (:id province)
                     :ciudadSuc (:id city)}]
    (:body (http/post url
                      {:form-params post-params}))))

(comment
  (def province (second (provinces/all)))
  (def city (first (cities/cities province)))
  (fetch-search-result {:province province :city city})
  )

(defn- build-rapipago
  [id-tag name-tag address-tag]
  (let [id (first (:content id-tag))
        name (first (:content name-tag))
        address (first (:content address-tag))]
  {:id id :name name :address address}))

(defn- parse-rapipagos
  [rapipagos-html-result]
  (let [reader (java.io.StringReader. rapipagos-html-result)
        html-result (html/html-resource reader)
        ids (html/select html-result [:.resultadosNumeroSuc])
        names (html/select html-result [:.resultadosTextWhite])
        resultadosText (html/select html-result [:.resultadosText])
        addresses (keep-indexed #(if (even? %1) %2) resultadosText)]

    (map build-rapipago ids names addresses)))

(comment
  (def result (fetch-search-result {:province province :city city}))
  (parse-rapipagos result)
  )

(defn search
  [{province :province city :city, :as filter}]
  (let [result (fetch-search-result filter)
        rapipagos (parse-rapipagos result)]
    (map #(merge filter %1) rapipagos)))

(comment
        (search {:province province :city city})
  )
