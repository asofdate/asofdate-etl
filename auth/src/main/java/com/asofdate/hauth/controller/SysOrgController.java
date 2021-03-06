package com.asofdate.hauth.controller;

import com.asofdate.hauth.authentication.JwtService;
import com.asofdate.hauth.entity.OrgEntity;
import com.asofdate.hauth.service.OrgService;
import com.asofdate.utils.Hret;
import com.asofdate.utils.RetMsg;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by hzwy23 on 2017/6/18.
 */
@RestController
@RequestMapping(value = "/v1/auth/org")
@Api(description = "系统管理--机构定义管理")
public class SysOrgController {


    @Autowired
    private OrgService orgService;

    @RequestMapping(method = RequestMethod.GET)
    public List findAll() {
        return orgService.findAll();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/sub")
    public List findSub(HttpServletResponse response, HttpServletRequest request) {
        String orgUnitId = request.getParameter("org_unit_id");
        return orgService.findSub(orgUnitId);
    }

    @RequestMapping(method = RequestMethod.POST)
    public String add(HttpServletResponse response, HttpServletRequest request) {
        OrgEntity orgEntity = parse(request);
        RetMsg retMsg = orgService.add(orgEntity);
        if (retMsg.checkCode()) {
            return Hret.success(retMsg);
        }
        response.setStatus(retMsg.getCode());
        return Hret.error(retMsg);
    }

    @RequestMapping(method = RequestMethod.PUT)
    public String update(HttpServletResponse response, HttpServletRequest request) {
        OrgEntity orgEntity = parse(request);
        RetMsg retMsg = orgService.update(orgEntity);
        if (retMsg.checkCode()) {
            return Hret.success(retMsg);
        }
        response.setStatus(retMsg.getCode());
        return Hret.error(retMsg);
    }


    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public String delete(HttpServletResponse response, HttpServletRequest request) {
        String json = request.getParameter("JSON");
        List<OrgEntity> list = new GsonBuilder().create().fromJson(json,
                new TypeToken<List<OrgEntity>>() {
                }.getType());

        RetMsg retMsg = orgService.delete(list);
        if (retMsg.checkCode()) {
            return Hret.success(retMsg);
        }
        response.setStatus(retMsg.getCode());
        return Hret.error(retMsg);
    }

    private OrgEntity parse(HttpServletRequest request) {
        OrgEntity orgEntity = new OrgEntity();
        String codeNumber = request.getParameter("Org_unit_id");
        orgEntity.setCode_number(codeNumber);
        orgEntity.setOrg_desc(request.getParameter("Org_unit_desc"));
        orgEntity.setUp_org_id(request.getParameter("Up_org_id"));
        orgEntity.setOrg_id(codeNumber);
        String userId = JwtService.getConnUser(request).getUserId();
        orgEntity.setCreate_user(userId);
        orgEntity.setModify_user(userId);
        return orgEntity;
    }
}
