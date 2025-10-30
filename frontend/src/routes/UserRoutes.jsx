import React from 'react'
import { Route, Routes } from 'react-router-dom';
import UserDetailPage from '../pages/UserDetailPage/UserDetailPage';
import UserRatingPage from '../pages/UserRatingPage/UserRatingPage';
import UserContentPage from '../pages/UserContentPage/UserContentPage';
import UserContentRatingPage from '../pages/UserContentRatingPage/UserContentRatingPage';
import UserContentWishPage from '../pages/UserContentWishPage/UserContentWishPage';
import UserCommentPage from '../pages/UserCommentPage/UserCommentPage';
import UserLikePage from '../pages/UserLilkePage/UserLilkePage';
import UserCollectionPage from '../pages/UserCollectionPage/UserCollectionPage';
import UserFollowPage from '../pages/UserFollowPage/UserFollowPage';

function UserRoutes({ userInfo, setUserInfo, openLogin }) {
  return (
    <Routes>
      <Route path=":id" element={<UserDetailPage userInfo={userInfo} setUserInfo={setUserInfo} openLogin={openLogin} />} />
      <Route path=":id/rating" element={<UserRatingPage userInfo={userInfo} openLogin={openLogin} />} />
      <Route path=":id/content/:contentType" element={<UserContentPage userInfo={userInfo} openLogin={openLogin} />} />
      <Route path=":id/content/:contentType/rating" element={<UserContentRatingPage userInfo={userInfo} openLogin={openLogin} />} />
      <Route path=":id/content/:contentType/wish" element={<UserContentWishPage userInfo={userInfo} openLogin={openLogin} />} />
      <Route path=":id/comment" element={<UserCommentPage userInfo={userInfo} openLogin={openLogin} />} />
      <Route path=":id/like" element={<UserLikePage userInfo={userInfo} openLogin={openLogin} />} />
      <Route path=":id/collection" element={<UserCollectionPage userInfo={userInfo} openLogin={openLogin} />} />
      <Route path=":id/follower" element={<UserFollowPage userInfo={userInfo} openLogin={openLogin} type={"follower"}/>} />
      <Route path=":id/following" element={<UserFollowPage userInfo={userInfo} openLogin={openLogin} type={"following"}/>} />
    </Routes>
  );
}

export default UserRoutes;
