import groovyx.net.http.HttpBuilder
import groovyx.net.http.optional.Download
import org.jsoup.nodes.Document

import static groovyx.net.http.HttpBuilder.configure

class Task3Bot {
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
        List<String> relatedTablesLink = tissPage.select('#parent-fieldname-text > p:nth-child(8) > a')
                .collect { it.attr('href') }

        Document relatedTablesPage = http.get { request.uri = relatedTablesLink[0] }
        List<String> errosTableLink = relatedTablesPage.select('#parent-fieldname-text > p:nth-child(2) > a')
                .collect {it.attr('href') }

        String targetLink = errosTableLink[0]
        List<String> targetLinkSplitted = targetLink.split("/")
        String fileName = targetLinkSplitted[targetLinkSplitted.size() - 1]
        String path = System.getProperty("user.dir") + "/app/src/main/groovy/downloads"

        File file = configure {
            request.uri = targetLink
        }.get() {
            Download.toFile(delegate, new File(path + "/${fileName}"))
        }
    }
}
