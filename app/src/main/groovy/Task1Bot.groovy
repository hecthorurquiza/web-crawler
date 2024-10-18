

import groovyx.net.http.HttpBuilder
import groovyx.net.http.optional.Download
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements

import static groovyx.net.http.HttpBuilder.configure

class Task1Bot {
    static HttpBuilder http = configure {
        request.uri = 'https://www.gov.br/ans/pt-br'
    }

    static void crawl() {
        Document homePage = http.get()
        List<String> linkPrestadorPage = homePage.select('#ce89116a-3e62-47ac-9325-ebec8ea95473 > div > a')
                .collect { it.attr('href') }

        Document providerPage = http.get { request.uri = linkPrestadorPage[0] }
        List<String> linkTissPage = providerPage.select('#content-core > div > div > div:nth-child(1) > a')
                .collect { it.attr('href') }

        Document tissPage = http.get { request.uri = linkTissPage[0] }
        List<String> linkPatternTissPage = tissPage.select('#parent-fieldname-text > p:nth-child(4) > a')
                .collect { it.attr('href') }

        Document patternTissPage = http.get { request.uri = linkPatternTissPage[0] }
        Elements table = patternTissPage.select('.table-responsive')

        List<String> tableLinks = []
        if (table != null) {
            Elements rows = table.select("tr");
            for (Element row : rows) {
                Elements cells = row.select("td");
                for (Element cell : cells) {
                    String links = cell.select("a").attr("href")
                    tableLinks.add(links)
                }
            }
        }
        else {
            print "Tabela não encontrada."
        }

        String targetLink = tableLinks[tableLinks.size() - 1]
        List<String> targeLinkSplitted = targetLink.split("/")
        String fileName = targeLinkSplitted[targeLinkSplitted.size() - 1]
        String path = System.getProperty("user.dir") + "/app/src/main/groovy/downloads"

        File file = configure {
            request.uri = tableLinks[tableLinks.size() - 1]
        }.get {
            Download.toFile(delegate, new File(path + "/${fileName}"))
        }
    }
}
