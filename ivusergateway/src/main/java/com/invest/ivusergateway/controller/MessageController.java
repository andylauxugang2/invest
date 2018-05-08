package com.invest.ivusergateway.controller;

import com.google.common.collect.Lists;
import com.invest.ivcommons.base.result.Result;
import com.invest.ivcommons.util.date.DateUtil;
import com.invest.ivuser.biz.service.MessageService;
import com.invest.ivuser.common.NotifyMessageTypeEnum;
import com.invest.ivuser.model.entity.NotifyMessage;
import com.invest.ivuser.model.entity.UserNotifyMessage;
import com.invest.ivuser.model.param.UserNotifyMessageParam;
import com.invest.ivuser.model.result.UserNotifyMessageResult;
import com.invest.ivusergateway.base.BaseController;
import com.invest.ivusergateway.common.constants.CodeEnum;
import com.invest.ivusergateway.model.request.ReadNotifyMessageReq;
import com.invest.ivusergateway.model.response.APIResponse;
import com.invest.ivusergateway.model.vo.NotifyMessageVO;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by xugang on 2016/9/6.
 */
@RestController
@RequestMapping(value = "/message")
public class MessageController extends BaseController {

    @Resource
    private MessageService messageService;

    @RequestMapping(value = "/getUserNotifyMessage", method = RequestMethod.GET, produces = {"application/json"})
    public APIResponse<List<NotifyMessageVO>> getUserNotifyMessage(@RequestParam(name = "userId") Long userId,
                                                                   @RequestParam(name = "messageStatus", required = false) Short messageStatus, //1-未读 2-已读 3-全部
                                                                   @RequestParam(name = "page") Integer page,
                                                                   @RequestParam(name = "limit") Integer limit,
                                                                   HttpServletRequest request) throws Exception {
        APIResponse<List<NotifyMessageVO>> result = new APIResponse<>();
        if (userId == null) {
            return APIResponse.createResult(CodeEnum.PARAM_ERROR);
        }

        //调用rpc服务
        UserNotifyMessageParam param = new UserNotifyMessageParam();
        param.setUserId(userId);
        param.setStatus(messageStatus);
        param.setPaged(true);
        param.setPage(page);
        param.setLimit(limit);
        UserNotifyMessageResult notifyMessageResult = messageService.getUserNotifyMessageList(param);
        if (notifyMessageResult.isFailed()) {
            return APIResponse.createResult(CodeEnum.FAILED);
        }

        List<UserNotifyMessage> userNotifyMessageList = notifyMessageResult.getUserNotifyMessages();
        if (CollectionUtils.isEmpty(userNotifyMessageList)) {
            result.setData(Lists.newArrayList());
            return result;
        }
        List<NotifyMessageVO> notifyMessageVOs = new ArrayList<>(userNotifyMessageList.size());
        notifyMessageVOs.addAll(userNotifyMessageList.stream().map(this::buildNotifyMessageVO).collect(Collectors.toList()));
        result.setData(notifyMessageVOs);
        result.setCount(notifyMessageResult.getCount());
        return result;
    }

    private NotifyMessageVO buildNotifyMessageVO(UserNotifyMessage userNotifyMessage) {
        NotifyMessageVO vo = new NotifyMessageVO();
        vo.setId(userNotifyMessage.getId());
        vo.setMessageId(userNotifyMessage.getMessageId());
        vo.setTitle(userNotifyMessage.getTitle());
        vo.setContent(userNotifyMessage.getContent());
        vo.setLink(userNotifyMessage.getLink());
        vo.setStatus(userNotifyMessage.getStatus());
        vo.setType(NotifyMessageTypeEnum.findByCode(userNotifyMessage.getType()).getDesc());
        vo.setCreateTime(DateUtil.dateToString(userNotifyMessage.getCreateTime(), DateUtil.DATE_FORMAT_DATETIME_COMMON));
        return vo;
    }

    private NotifyMessageVO buildNotifyMessageVO(NotifyMessage notifyMessage) {
        NotifyMessageVO vo = new NotifyMessageVO();
        vo.setId(notifyMessage.getId());
        vo.setTitle(notifyMessage.getTitle());
        vo.setContent(notifyMessage.getContent());
        vo.setLink(notifyMessage.getLink());
        vo.setStatus(notifyMessage.getStatus());
        vo.setType(NotifyMessageTypeEnum.findByCode(notifyMessage.getType()).getDesc());
        vo.setCreateTime(DateUtil.dateToString(notifyMessage.getCreateTime(), DateUtil.DATE_FORMAT_DATETIME_COMMON));
        return vo;
    }

    /**
     * 读消息
     */
    @RequestMapping(value = "/readUserNotifyMessage", method = RequestMethod.POST, produces = {"application/json"})
    public APIResponse<Void> readUserNotifyMessage(@RequestBody ReadNotifyMessageReq req) throws Exception {
        APIResponse<Void> result = new APIResponse<>();
        Long userId = req.getUserId();
        Long userMessageId = req.getUserMessageId();

        if (userId == null || userMessageId == null) {
            return APIResponse.createResult(CodeEnum.PARAM_ERROR);
        }

        UserNotifyMessage userNotifyMessage = new UserNotifyMessage();
        userNotifyMessage.setUserId(userId);
        userNotifyMessage.setId(userMessageId);
        userNotifyMessage.setStatus(UserNotifyMessage.STATUS_READED);
        //调用rpc服务
        Result modifyUserNotifyMessage = messageService.modifyUserNotifyMessage(userNotifyMessage);
        if (modifyUserNotifyMessage.isFailed()) {
            return APIResponse.createResult(CodeEnum.FAILED.getCode(), modifyUserNotifyMessage.getErrorMsg());
        }

        return result;
    }

    /**
     * 批量读消息
     */
    @RequestMapping(value = "/readUserNotifyMessageBatch", method = RequestMethod.POST, produces = {"application/json"})
    public APIResponse<Void> readUserNotifyMessageBatch(@RequestBody ReadNotifyMessageReq req) throws Exception {
        APIResponse<Void> result = new APIResponse<>();
        List<Long> userMessageIds = req.getUserMessageIds();

        if (CollectionUtils.isEmpty(userMessageIds)) {
            return APIResponse.createResult(CodeEnum.PARAM_ERROR);
        }

        UserNotifyMessage userNotifyMessage = new UserNotifyMessage();
        userNotifyMessage.setStatus(UserNotifyMessage.STATUS_READED);
        //调用rpc服务
        Result modifyUserNotifyMessage = messageService.modifyUserNotifyMessageBatch(userMessageIds, userNotifyMessage);
        if (modifyUserNotifyMessage.isFailed()) {
            return APIResponse.createResult(CodeEnum.FAILED.getCode(), modifyUserNotifyMessage.getErrorMsg());
        }

        return result;
    }

    @RequestMapping(value = "/getUserNotifyMessageDetail", method = RequestMethod.GET, produces = {"application/json"})
    public APIResponse<NotifyMessageVO> getUserNotifyMessageDetail(@RequestParam(name = "messageId") Long messageId,
                                                                   HttpServletRequest request) throws Exception {
        APIResponse<NotifyMessageVO> result = new APIResponse<>();
        if (messageId == null) {
            return APIResponse.createResult(CodeEnum.PARAM_ERROR);
        }

        //调用rpc服务
        UserNotifyMessageResult notifyMessageResult = messageService.getNotifyMessageById(messageId);
        if (notifyMessageResult.isFailed()) {
            return APIResponse.createResult(CodeEnum.FAILED);
        }

        NotifyMessage notifyMessage = notifyMessageResult.getNotifyMessage();
        if (notifyMessage == null) {
            result.setData(new NotifyMessageVO());
            return result;
        }

        result.setData(buildNotifyMessageVO(notifyMessage));
        return result;
    }
}
