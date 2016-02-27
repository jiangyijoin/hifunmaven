package com.hifun.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hifun.base.session.SessionUser;
import com.hifun.bean.Banner;
import com.hifun.bean.HiThings;
import com.hifun.bean.Menu;
import com.hifun.dao.IHeadDao;
import com.hifun.dao.IUserAuthenDao;
import com.hifun.service.IHeadService;

@Service
public class HeadServiceImpl implements IHeadService {

    @Autowired
    private IHeadDao headDao;

    @Autowired
    private IUserAuthenDao userAuthenDao;

    @Override
    public List<Banner> queryBannerValidateList() {
        return headDao.queryBannerValidateList();
    }

    @Override
    public List<Menu> queryMenuValidateList() {
        return headDao.queryMenuValidateList();
    }

    @Override
    public Integer queryTodayIsSign(String username, String nowdate) {
        return headDao.queryTodayIsSign(username, nowdate);
    }

    @Override
    public void insertSign(String username, String nowdate, int signOrder) {
        // 保存签到记录
        headDao.insertSign(username, nowdate, signOrder);
        // 增加经验
        userAuthenDao.updateUserExp(username, 6);
    }

    @Override
    public Integer querySignCount(String nowdate) {
        return headDao.querySignCount(nowdate);
    }

    @Override
    public Integer querySignCount(String nowdate, String username) {
        return headDao.querySignCount(nowdate, username);
    }

    @Override
    public List<SessionUser> queryTopTenUsers() {
        return headDao.queryTopTenUsers();
    }

    @Override
    public List<HiThings> queryHiThingsByStatus(int status) {
        return headDao.queryHiThingsByStatus(status);
    }

    @Override
    public List<HiThings> queryHiThingsByStatus(String username, int status) {
        return headDao.queryHiThingsByStatus(username, status);
    }

    @Override
    public void insertHiThings(String content, String username,
            String nowdate) {
        headDao.insertHiThings(content, username, nowdate);
    }

    @Override
    public int queryEvaluateRelateIdByName(String relateTypeName) {
        return headDao.queryEvaluateRelateIdByName(relateTypeName);
    }

    @Override
    public void insertEvaluate(String username, String nowdate, int evaluateId,
            int evaluateTypeId, int relateTypeId) {
        headDao.insertEvaluate(username, nowdate, evaluateId, evaluateTypeId,
            relateTypeId);
        headDao.updateHiThingsNum(evaluateId, evaluateTypeId);
    }

    @Override
    public int queryEvaluateCount(int evaluateId, String username) {
        return headDao.queryEvaluateCount(evaluateId, username);
    }

    @Override
    public int queryTotalZan(String nowdate, String username,
            int relateTypeId) {
        return headDao.queryTotalZan(nowdate, username, relateTypeId);
    }

}