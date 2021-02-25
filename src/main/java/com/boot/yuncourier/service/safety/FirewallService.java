package com.boot.yuncourier.service.safety;


import com.boot.yuncourier.entity.safety.Firewall;
import com.boot.yuncourier.entity.user.User;

import java.util.List;

/**
 * @Author: skwen
 * @Description: FiewwallService-防火墻接口
 * @Date: 2020-02-01
 */

public interface FirewallService {
    int addFirewallByFirewall(Firewall firewall);
    List<Firewall> getFirewallListByUser(User user);
    int deleteFirewallByFirewall(Firewall firewall);
    Firewall getFirewallInfoByFirewall(Firewall firewall);
    Firewall getFirewallInfoByIpAndSoftwareId(Firewall firewall);
    int updateFirewallByFirewall(Firewall firewall);
    List<Firewall> getFirewallListByFirewall(Firewall firewall);
    Firewall checkFirewallByFirewall(Firewall firewall);
}
