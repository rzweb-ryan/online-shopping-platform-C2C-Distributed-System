package us.rzweb.taotao.search.handler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.springframework.beans.factory.annotation.Autowired;
import us.rzweb.taotao.search.pojo.Item;
import us.rzweb.taotao.search.service.ItemService;

import java.io.IOException;

/**
 * Created by RZ on 9/19/16.
 */
public class ItemHandler {

    private ObjectMapper MAPPER = new ObjectMapper();
    @Autowired
    private HttpSolrServer httpSolrServer;

    @Autowired
    private ItemService itemService;
    public void executeMessage(String msg) {
        try {
            try {
                JsonNode jsonNode = MAPPER.readTree(msg);
                Long itemId = jsonNode.get("itemId").asLong();
                String type = jsonNode.get("type").asText();
                if (StringUtils.equals(type,"insert") || StringUtils.equals(type, "update")) {
                    Item item = this.itemService.queryItemById(itemId);
                    this.httpSolrServer.addBean(item);
                    this.httpSolrServer.commit();
                }else if (StringUtils.equals(type, "delete")) {
                    this.httpSolrServer.deleteById(itemId.toString());
                    this.httpSolrServer.commit();
                }
            } catch (SolrServerException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
