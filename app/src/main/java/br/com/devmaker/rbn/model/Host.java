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


import java.io.Serializable;

public class Host implements Serializable {
    @com.google.gson.annotations.SerializedName("id")
    private String id = null;
    @com.google.gson.annotations.SerializedName("name")
    private String name = null;
    @com.google.gson.annotations.SerializedName("picture")
    private String picture = null;

    /**
     * Gets id
     *
     * @return id
     **/
    public String getId() {
        return id;
    }

    /**
     * Sets the value of id.
     *
     * @param id the new value
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Gets name
     *
     * @return name
     **/
    public String getName() {
        return name;
    }

    /**
     * Sets the value of name.
     *
     * @param name the new value
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets picture
     *
     * @return picture
     **/
    public String getPicture() {
        return picture;
    }

    /**
     * Sets the value of picture.
     *
     * @param picture the new value
     */
    public void setPicture(String picture) {
        this.picture = picture;
    }

}
