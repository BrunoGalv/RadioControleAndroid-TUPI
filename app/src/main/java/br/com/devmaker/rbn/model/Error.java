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


public class Error {
    @com.google.gson.annotations.SerializedName("error")
    private String error = null;
    @com.google.gson.annotations.SerializedName("message")
    private String message = null;

    /**
     * Gets error
     *
     * @return error
     **/
    public String getError() {
        return error;
    }

    /**
     * Sets the value of error.
     *
     * @param error the new value
     */
    public void setError(String error) {
        this.error = error;
    }

    /**
     * Gets message
     *
     * @return message
     **/
    public String getMessage() {
        return message;
    }

    /**
     * Sets the value of message.
     *
     * @param message the new value
     */
    public void setMessage(String message) {
        this.message = message;
    }

}
