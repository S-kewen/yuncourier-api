package com.boot.yuncourier.service.impl.safety;

import com.boot.yuncourier.dao.safety.FirewallMapper;
import com.boot.yuncourier.entity.safety.Firewall;
import com.boot.yuncourier.entity.user.User;
import com.boot.yuncourier.service.safety.FirewallService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author: skwen
 * @Description: FirewallServiceImpl-防火墻service
 * @Date: 2020-02-01
 */

@Component
public class FirewallServiceImpl implements FirewallService {

    @Autowired
    private FirewallMapper firewallMapper;
    @Override
    public int addFirewallByFirewall(Firewall firewall) {
        return firewallMapper.addFirewallByFirewall(firewall);
    }
    @Override
    public List<Firewall> getFirewallListByUser(User user) {
        return firewallMapper.getFirewallListByUser(user);
    }
    @Override
    public int deleteFirewallByFirewall(Firewall firewall) {
        return firewallMapper.deleteFirewallByFirewall(firewall);
    }
    @Override
    public Firewall getFirewallInfoByFirewall(Firewall firewall) {
        return firewallMapper.getFirewallInfoByFirewall(firewall);
    }
    @Override
    public Firewall getFirewallInfoByIpAndSoftwareId(Firewall firewall) {
        return firewallMapper.getFirewallInfoByIpAndSoftwareId(firewall);
    }
    @Override
    public int updateFirewallByFirewall(Firewall firewall) {
        return firewallMapper.updateFirewallByFirewall(firewall);
    }
    @Override
    public List<Firewall> getFirewallListByFirewall(Firewall firewall) {
        return firewallMapper.getFirewallListByFirewall(firewall);
    }

    @Override
    public Firewall checkFirewallByFirewall(Firewall firewall) {
        firewall.setType(1);
        List<Firewall> white = firewallMapper.getFirewallListByFirewall(firewall);//获取白名单
        for(int i=0;i<white.size();i++){
            if(firewall.getIp().equals(white.get(i).getIp()) || white.get(i).getIp().equals("*.*.*.*")){
                return null;
            }else{
                String[]  ips = firewall.getIp().split(".");
                String[]  arr = white.get(i).getIp().split(".");
                if(arr.length==4 && ips.length==4){
                    int succNum=0;
                    for(int ii=0;i<arr.length;ii++){
                        if(arr[ii].equals(ips[ii]) || arr[ii].equals("*")){
                            succNum++;
                        }
                    }
                    if(succNum==4){
                        return null;
                    }
                }
            }
        }//白名单判断结束
        firewall.setType(2);
        List<Firewall> black = firewallMapper.getFirewallListByFirewall(firewall);//获取黑名单
        for(int i=0;i<black.size();i++){
            if(firewall.getIp().equals(black.get(i).getIp()) || black.get(i).getIp().equals("*.*.*.*")){
                return black.get(i);
            }else{
                String[]  ips = firewall.getIp().split(".");
                String[]  arr = black.get(i).getIp().split(".");
                if(arr.length==4 && ips.length==4){
                    int succNum=0;
                    for(int ii=0;i<arr.length;ii++){
                        if(arr[ii].equals(ips[ii]) || arr[ii].equals("*")){
                            succNum++;
                        }
                    }
                    if(succNum==4){
                        return black.get(i);
                    }
                }
            }
        }//黑名单判断结束
        return null;
    }


}
