import groovyx.net.http.HttpBuilder
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements

import static groovyx.net.http.HttpBuilder.configure

class Task2Bot {
    static HttpBuilder http = configure {
        request.uri = 'https://www.gov.br/ans/pt-br'
    }

    private static void toCsv(String path, List<List<String>> data) {
        File file = new File(path + "/hitoricVersionsTISS.csv")
        file.withWriter { writer ->
            data.forEach { row ->
                writer.writeLine(row.join(', '))
            }
        }
        print("Arquivo gerado com sucesso!")
    }

    static void crawl() {
        Document homePage = http.get()
        List<String> linkPrestadorPage = homePage.select('#ce89116a-3e62-47ac-9325-ebec8ea95473 > div > a')
                .collect { it.attr('href') }

        Document providerPage = http.get { request.uri = linkPrestadorPage[0] }
        List<String> linkTissPage = providerPage.select('#content-core > div > div > div:nth-child(1) > a')
                .collect { it.attr('href') }

        Document tissPage = http.get { request.uri = linkTissPage[0] }
        List<String> historicVersionsPageLink = tissPage.select('#parent-fieldname-text > p:nth-child(6) > a')
                .collect { it.attr('href') }

        Document historicPage = http.get { request.uri = historicVersionsPageLink[0] }
        Elements table = historicPage.select('#parent-fieldname-text > table')

        List<String> tableHeaders = []
        List<List<String>> tableData = []
        List<String> rowData = []
        List<List<String>> data2Csv = []

        if (table != null) {
            Elements rows = table.select("tr")
            for (Element row : rows) {
                Elements headers = row.select("th");
                Elements data = row.select("td");

                for (Element header : headers) {
                    tableHeaders.add(header.text())
                }
                for (Element value : data) {
                    rowData.add(value.text())
                }
                tableData.add(rowData)
                rowData = []
            }
        }
        else {
            print "Tabela não encontrada."
        }

        int firstIndex = 0
        for (int i = 0; i < tableData.size(); i++) {
            if (tableData.reverse()[i].contains("Jan/2016")) {
                firstIndex = i
                break
            }
        }

        data2Csv.add(tableHeaders.take(3))
        for  (int i = firstIndex; i < tableData.size() - 1; i++) {
            data2Csv.add(tableData.reverse()[i].take(3))
        }
        String path = System.getProperty("user.dir") + "/app/src/main/groovy/data"
        toCsv(path, data2Csv)
    }
}
