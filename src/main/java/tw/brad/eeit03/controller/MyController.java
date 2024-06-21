package tw.brad.eeit03.controller;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.web.bind.annotation.*;
import tw.brad.eeit03.mapper.MarketRowMapper;
import tw.brad.eeit03.model.Cust;
import tw.brad.eeit03.model.Market;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class MyController {
    @Autowired
    private NamedParameterJdbcTemplate jdbc;

    @PostMapping("/cust1")
    public String insert1(@RequestBody Cust cust){
        String sql = "INSERT INTO cust (cname,tel,birthday) VALUES ('test1','123','1999-01-02')";
        Map<String,Object> map = new HashMap<>();
        jdbc.update(sql, map);
        return "insert()";
    }
    @PostMapping("/cust2")
    public String insert2(@RequestBody Cust cust){
        String sql = "INSERT INTO cust (cname,tel,birthday) VALUES (:cname,:tel,:birthday)";
        Map<String,Object> map = new HashMap<>();
        map.put("cname", cust.getCname());
        map.put("tel", cust.getTel());
        map.put("birthday", cust.getBirthday());
        jdbc.update(sql, map);
        return "insert()";
    }

    @PostMapping("/market")
    public Boolean insertMarkets() throws Exception{
        URL url = new URL("https://data.moa.gov.tw/Service/OpenData/FromM/FarmerMarketData.aspx");
        InputStreamReader ir = new InputStreamReader(url.openConnection().getInputStream());
        BufferedReader reader = new BufferedReader(ir);
        String line; StringBuffer sb = new StringBuffer();
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        reader.close();

        String json = sb.toString()
                .replace("市集名稱", "name")
                .replace("電話","tel");

        ObjectMapper objectMapper = new ObjectMapper();
        List<Market> marketList = objectMapper.readValue(json,
                new TypeReference<List<Market>>(){});
        for(Market market : marketList){
            System.out.println(market.getName());
        }

//        for (Market market : marketList) {
//            String sql = "INSERT INTO market (name,tel) VALUES (:name,:tel)";
//            Map<String, Object> map = new HashMap<>();
//            map.put("name", market.getName());
//            map.put("tel", market.getTel());
//            jdbc.update(sql, map);
//        }

        String sql = "INSERT INTO market (name,tel) VALUES (:name,:tel)";
        MapSqlParameterSource[] sources = new MapSqlParameterSource[marketList.size()];
        for (int i=0; i<marketList.size(); i++){
            sources[i] = new MapSqlParameterSource()
                    .addValue("name", marketList.get(i).getName())
                    .addValue("tel", marketList.get(i).getTel());
        }
        jdbc.batchUpdate(sql, sources);

        return true;
    }

    @GetMapping("/market/{id}")
    public Market query(@PathVariable Integer id){
        String sql = "SELECT id, name, tel FROM market WHERE id = :id";
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        Market market = jdbc.queryForObject(sql, map, new MarketRowMapper());
        //System.out.println(market.getName());

        List<Market> marketList = jdbc.query(sql, map, new MarketRowMapper());

        return market;
    }

}
