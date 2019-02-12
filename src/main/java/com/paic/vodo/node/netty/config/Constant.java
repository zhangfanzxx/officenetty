package com.paic.vodo.node.netty.config;

public interface Constant {
    String OFFICE_SERVICE_USING = "officeServiceUseing";
    String LOCK_FILENAME = "lockFilename";
    String SESSION_LOGIN_KEY_NAME = "loginstaff";
    String SESSION_MY_FOLDER_PERMISSION = "loginpermission";
    String SHARE_OFFICE_NAME = "shareOfficeName";
    String INVITE_TO_REGIST = "inviteToRegist";

    /**
     * 文件上传允许的类型
     */
    String S_NAME = "doc,docx,xls,xlsx,ppt,pdf,vsd,vsdx";

    String DOCUMENT_TYPE_WORLD = "text";
    String DOCUMENT_TYPE_PPT = "presentation";
    String DOCUMENT_TYPE_EXCEL = "spreadsheet";
    String DOCUMENT_TYPE_MIND = "mindjson";
    String DOCUMENT_TYPE_CHART="flowchart";
    String DOCUMENT_TYPE_MARKDOWN="markdown";
    String DOCUMENT_TYPE_PDF="pdf";
    String DOCUMENT_TYPE_VSD="vsd";
    String DOCUMENT_TYPE_TABLE="tablejson";

    /**
     * 文件夹目录的格式
     */
    Integer FOLDER_LAYER = 3;

    Integer EDITOR_PERMITION = 1;
    Integer READ_ONLY_PERMITION = 2;
    String READ_ONLY_STRING = "ReadOnly";
    String EDITOR_STRING = "Editor";
    Integer NO_PERMITION = 3;

    String FIND_EMAIL_BUT_NOT_EXIST = "用户不存在通过邮件邀请";

    String FOLDER_TYPE = "FOLDER";
    String OFFICE_TYPE = "FILE";

    String REDIS_CACHE_PARENT_DIR="dirParentCache";
    Integer CUN_ZAI=1;
    Integer BU_CUN_ZAI=0;

}

