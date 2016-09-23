package us.rzweb.taotao.web.bean;

import org.apache.commons.lang3.StringUtils;

public class Item extends us.rzweb.taotao.manage.pojo.Item {

    public String[] getImages() {
        return StringUtils.split(super.getImage(), ',');
    }

}
