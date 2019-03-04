package vedam.subkuch.network.handler;

import vedam.subkuch.network.models.getLikeDislikeResponse.GetLikeDislikeStatus;

public interface GetLikeDislikeHandler extends BaseHandler {
    void onSuccess(GetLikeDislikeStatus response);
}
