package com.tbs.tobosutype.utils;

/**
 * Created by Mr.Lin on 2018/1/22 11:48.
 * Android 端文 点击字节流获取工具
 */

public class TbsDcCls {
    //ct  生成数据的时间

    //uid  用户的UID
    //pn 产品的名称
    //pv 产品的版本
    //di 设备ID 生成规则 Android:md5(ani+imei+mac)
    //si 回话ID android+ios:重新启动刷新session ID    生成规则：md5(di+current_time+random_str)
    //sr 设备的分辨率
    //dm 设备型号 MI 3W
    //sv 设备系统版本 Android 6.0
    //lng	经度	longitude	float	<12(116.203048)	未获取到置为空
    //lat	纬度	latitude	float	<12(40.010497)	未获取到置为空
    //mac	MAC地址	MAC	string	=17(00:01:6C:06:A6:29)	未获取到置为空
    //imei	IMEI号	IMEI	string	=15(493002407599521)	未获取到置为空
    //ani	安卓ID	android ID	string	=16(9774d56d682e549c)	android api获得
    //no	网络运营商	network operate	string	<8(CMCC,CUCC,CTCC,WIFI)	未获取到置为空
}
