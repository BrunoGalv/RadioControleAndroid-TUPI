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

package br.com.devmaker.rbn.util;

import br.com.devmaker.rbn.model.Group;
import br.com.devmaker.rbn.model.Settings;
import br.com.devmaker.rbn.model.Hosts;
import br.com.devmaker.rbn.model.Participant;
import br.com.devmaker.rbn.model.Podcasts;
import br.com.devmaker.rbn.model.Posts;
import br.com.devmaker.rbn.model.Post;
import br.com.devmaker.rbn.model.Comments;
import br.com.devmaker.rbn.model.Comment;
import br.com.devmaker.rbn.model.Programs;
import br.com.devmaker.rbn.model.Promotions;


@com.amazonaws.mobileconnectors.apigateway.annotation.Service(endpoint = "https://6qpur9jvsk.execute-api.us-east-1.amazonaws.com/prod")
public interface RadiocontroleClient {


    /**
     * A generic invoker to invoke any API Gateway endpoint.
     * @param request
     * @return ApiResponse
     */
    com.amazonaws.mobileconnectors.apigateway.ApiResponse execute(com.amazonaws.mobileconnectors.apigateway.ApiRequest request);

    /**
     *
     *
     * @param radioGroupId
     * @return Group
     */
    @com.amazonaws.mobileconnectors.apigateway.annotation.Operation(path = "/radiogroup/{radioGroupId}", method = "GET")
    Group radiogroupRadioGroupIdGet(
            @com.amazonaws.mobileconnectors.apigateway.annotation.Parameter(name = "radioGroupId", location = "path")
                    String radioGroupId);

    /**
     *
     *
     * @param radioid
     * @return Settings
     */
    @com.amazonaws.mobileconnectors.apigateway.annotation.Operation(path = "/{radioid}", method = "GET")
    Settings radioidGet(
            @com.amazonaws.mobileconnectors.apigateway.annotation.Parameter(name = "radioid", location = "path")
                    String radioid);

    /**
     *
     *
     * @param radioid
     * @return Hosts
     */
    @com.amazonaws.mobileconnectors.apigateway.annotation.Operation(path = "/{radioid}/hosts", method = "GET")
    Hosts radioidHostsGet(
            @com.amazonaws.mobileconnectors.apigateway.annotation.Parameter(name = "radioid", location = "path")
                    String radioid);

    /**
     *
     *
     * @param radioid
     * @param body
     * @return void
     */
    @com.amazonaws.mobileconnectors.apigateway.annotation.Operation(path = "/{radioid}/participants", method = "POST")
    void radioidParticipantsPost(
            @com.amazonaws.mobileconnectors.apigateway.annotation.Parameter(name = "radioid", location = "path")
                    String radioid,
            Participant body);

    /**
     *
     *
     * @param radioid
     * @return Podcasts
     */
    @com.amazonaws.mobileconnectors.apigateway.annotation.Operation(path = "/{radioid}/podcasts", method = "GET")
    Podcasts radioidPodcastsGet(
            @com.amazonaws.mobileconnectors.apigateway.annotation.Parameter(name = "radioid", location = "path")
                    String radioid);

    /**
     *
     *
     * @param radioid
     * @param lastKey
     * @return Posts
     */
    @com.amazonaws.mobileconnectors.apigateway.annotation.Operation(path = "/{radioid}/posts", method = "GET")
    Posts radioidPostsGet(
            @com.amazonaws.mobileconnectors.apigateway.annotation.Parameter(name = "radioid", location = "path")
                    String radioid,
            @com.amazonaws.mobileconnectors.apigateway.annotation.Parameter(name = "lastKey", location = "query")
                    String lastKey);

    /**
     *
     *
     * @param radioid
     * @param body
     * @return Post
     */
    @com.amazonaws.mobileconnectors.apigateway.annotation.Operation(path = "/{radioid}/posts", method = "POST")
    Post radioidPostsPost(
            @com.amazonaws.mobileconnectors.apigateway.annotation.Parameter(name = "radioid", location = "path")
                    String radioid,
            Post body);

    /**
     *
     *
     * @param postId
     * @param radioid
     * @param lastKey
     * @return Comments
     */
    @com.amazonaws.mobileconnectors.apigateway.annotation.Operation(path = "/{radioid}/posts/{postId}/comments", method = "GET")
    Comments radioidPostsPostIdCommentsGet(
            @com.amazonaws.mobileconnectors.apigateway.annotation.Parameter(name = "postId", location = "path")
                    String postId,
            @com.amazonaws.mobileconnectors.apigateway.annotation.Parameter(name = "radioid", location = "path")
                    String radioid,
            @com.amazonaws.mobileconnectors.apigateway.annotation.Parameter(name = "lastKey", location = "query")
                    String lastKey);

    /**
     *
     *
     * @param postId
     * @param radioid
     * @param body
     * @return Comment
     */
    @com.amazonaws.mobileconnectors.apigateway.annotation.Operation(path = "/{radioid}/posts/{postId}/comments", method = "POST")
    Comment radioidPostsPostIdCommentsPost(
            @com.amazonaws.mobileconnectors.apigateway.annotation.Parameter(name = "postId", location = "path")
                    String postId,
            @com.amazonaws.mobileconnectors.apigateway.annotation.Parameter(name = "radioid", location = "path")
                    String radioid,
            Comment body);

    /**
     *
     *
     * @param radioid
     * @return Programs
     */
    @com.amazonaws.mobileconnectors.apigateway.annotation.Operation(path = "/{radioid}/programs", method = "GET")
    Programs radioidProgramsGet(
            @com.amazonaws.mobileconnectors.apigateway.annotation.Parameter(name = "radioid", location = "path")
                    String radioid);

    /**
     *
     *
     * @param radioid
     * @return Promotions
     */
    @com.amazonaws.mobileconnectors.apigateway.annotation.Operation(path = "/{radioid}/promotions", method = "GET")
    Promotions radioidPromotionsGet(
            @com.amazonaws.mobileconnectors.apigateway.annotation.Parameter(name = "radioid", location = "path")
                    String radioid);

}

