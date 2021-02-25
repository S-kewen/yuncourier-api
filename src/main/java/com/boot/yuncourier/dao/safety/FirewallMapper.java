package com.boot.yuncourier.dao.safety;

import com.boot.yuncourier.entity.safety.Firewall;
import com.boot.yuncourier.entity.user.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Author: skwen
 * @Description: FirewallMapper-防火墻dao
 * @Date: 2020-02-01
 */
@Mapper
public interface FirewallMapper {
    int addFirewallByFirewall(Firewall firewall);
    List<Firewall> getFirewallListByUser(User user);
    int deleteFirewallByFirewall(Firewall firewall);
    Firewall getFirewallInfoByFirewall(Firewall firewall);
    Firewall getFirewallInfoByIpAndSoftwareId(Firewall firewall);
    int updateFirewallByFirewall(Firewall firewall);
    List<Firewall> getFirewallListByFirewall(Firewall firewall);
}