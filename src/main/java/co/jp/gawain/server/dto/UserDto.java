package co.jp.gawain.server.dto;

import java.sql.Timestamp;

public class UserDto {
    private int userId;
    private String userName;
    private String eMail;
    private Timestamp created;
    private Timestamp updated;
    private boolean deleteFlag;

    // コンストラクタ
    public UserDto() {
    }

    /**
     * ユーザーID
     * @return int
     */
    public int getUserId() {
        return this.userId;
    }
    /**
     * ユーザー名
     * @return String
     */
    public String getUserName() {
        return this.userName;
    }
    /**
     * メールアドレス
     * @return String
     */
    public String getEMail() {
        return this.eMail;
    }
    /**
     * 作成日
     * @return Timestamp
     */
    public Timestamp getCreated() {
        return this.created;
    }
    /**
     * 更新日
     * @return Timestamp
     */
    public Timestamp getUpdated() {
        return this.updated;
    }
    /**
     * 削除フラグ
     * @return boolean
     */
    public boolean getDeleteFlag() {
        return this.deleteFlag;
    }

    /**
     * ユーザーID
     * @param userId
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }
    /**
     * ユーザー名
     * @param userName
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }
    /**
     * メールアドレス
     * @param eMail
     */
    public void setEMail(String eMail) {
        this.eMail = eMail;
    }
    /**
     * 作成日
     * @param created
     */
    public void setCreated(Timestamp created) {
        this.created = created;
    }
    /**
     * 更新日
     * @param updated
     */
    public void setUpdated(Timestamp updated) {
        this.updated = updated;
    }
    /**
     * 削除フラグ
     * @param deleteFlag
     */
    public void setDeleteFlag(boolean deleteFlag) {
        this.deleteFlag = deleteFlag;
    }
}
