package tw.brad.eeit03.mapper;

import org.springframework.jdbc.core.RowMapper;
import tw.brad.eeit03.model.Market;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MarketRowMapper implements RowMapper<Market> {
    @Override
    public Market mapRow(ResultSet rs, int rowNum) throws SQLException {
        //System.out.println("debug:" +rs.next());
        Market market = new Market();
        market.setId(rs.getInt("id"));
        market.setName(rs.getString("name"));
        market.setTel(rs.getString("tel"));
        return market;
    }
}
