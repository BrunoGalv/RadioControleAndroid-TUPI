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


public class Participant {
    @com.google.gson.annotations.SerializedName("promotionId")
    private String promotionId = null;
    @com.google.gson.annotations.SerializedName("email")
    private String email = null;
    @com.google.gson.annotations.SerializedName("cep")
    private String cep = null;
    @com.google.gson.annotations.SerializedName("cpf")
    private String cpf = null;
    @com.google.gson.annotations.SerializedName("fone")
    private String fone = null;
    @com.google.gson.annotations.SerializedName("radioId")
    private String radioId = null;
    @com.google.gson.annotations.SerializedName("name")
    private String name = null;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets promotionId
     *
     * @return promotionId
     **/
    public String getPromotionId() {
        return promotionId;
    }

    /**
     * Sets the value of promotionId.
     *
     * @param promotionId the new value
     */
    public void setPromotionId(String promotionId) {
        this.promotionId = promotionId;
    }

    /**
     * Gets email
     *
     * @return email
     **/
    public String getEmail() {
        return email;
    }

    /**
     * Sets the value of email.
     *
     * @param email the new value
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets cep
     *
     * @return cep
     **/
    public String getCep() {
        return cep;
    }

    /**
     * Sets the value of cep.
     *
     * @param cep the new value
     */
    public void setCep(String cep) {
        this.cep = cep;
    }

    /**
     * Gets cpf
     *
     * @return cpf
     **/
    public String getCpf() {
        return cpf;
    }

    /**
     * Sets the value of cpf.
     *
     * @param cpf the new value
     */
    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    /**
     * Gets fone
     *
     * @return fone
     **/
    public String getFone() {
        return fone;
    }

    /**
     * Sets the value of fone.
     *
     * @param fone the new value
     */
    public void setFone(String fone) {
        this.fone = fone;
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
