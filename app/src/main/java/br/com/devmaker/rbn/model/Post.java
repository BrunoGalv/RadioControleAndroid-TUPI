/*
 * Copyright 2010-2016 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 *  http://aws.amazon.com/apache2.0
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package br.com.devmaker.rbn.model;

public class Post {
    @com.google.gson.annotations.SerializedName("timestamp")
    private long timestamp;
    @com.google.gson.annotations.SerializedName("authorId")
    private String authorId = null;
    @com.google.gson.annotations.SerializedName("authorName")
    private String authorName = null;
    @com.google.gson.annotations.SerializedName("authorPicture")
    private String authorPicture = null;
    @com.google.gson.annotations.SerializedName("type")
    private String type = null;
    @com.google.gson.annotations.SerializedName("content")
    private String content = null;
    @com.google.gson.annotations.SerializedName("attachment")
    private String attachment = null;
    @com.google.gson.annotations.SerializedName("radioId")
    private String radioId = null;

    /**
     * Gets timestamp
     *
     * @return timestamp
     **/
    public Long getTimestamp() {
        return timestamp;
    }

    /**
     * Sets the value of timestamp.
     *
     * @param timestamp the new value
     */
    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * Gets authorId
     *
     * @return authorId
     **/
    public String getAuthorId() {
        return authorId;
    }

    /**
     * Sets the value of authorId.
     *
     * @param authorId the new value
     */
    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    /**
     * Gets authorName
     *
     * @return authorName
     **/
    public String getAuthorName() {
        return authorName;
    }

    /**
     * Sets the value of authorName.
     *
     * @param authorName the new value
     */
    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    /**
     * Gets authorPicture
     *
     * @return authorPicture
     **/
    public String getAuthorPicture() {
        return authorPicture;
    }

    /**
     * Sets the value of authorPicture.
     *
     * @param authorPicture the new value
     */
    public void setAuthorPicture(String authorPicture) {
        this.authorPicture = authorPicture;
    }

    /**
     * Gets type
     *
     * @return type
     **/
    public String getType() {
        return type;
    }

    /**
     * Sets the value of type.
     *
     * @param type the new value
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Gets content
     *
     * @return content
     **/
    public String getContent() {
        return content;
    }

    /**
     * Sets the value of content.
     *
     * @param content the new value
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * Gets attachment
     *
     * @return attachment
     **/
    public String getAttachment() {
        return attachment;
    }

    /**
     * Sets the value of attachment.
     *
     * @param attachment the new value
     */
    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }

    /**
     * Gets radioId
     *
     * @return radioId
     **/
    public String getRadioId() {
        return radioId;
    }

    /**
     * Sets the value of radioId.
     *
     * @param radioId the new value
     */
    public void setRadioId(String radioId) {
        this.radioId = radioId;
    }

}
