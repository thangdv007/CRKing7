import httpRequest from "~/constants/httpRequest";
import { Article } from "~/types/article.type";
import { SuccessResponse } from "~/types/utils.type";

const articleApi = {
  getArticleHome : () => httpRequest.get<SuccessResponse<Article>,any>(`/api/article/home`),
  detailArticle : (id) => httpRequest.get<SuccessResponse<Article>,any>(`/api/article/${id}`),
  relatedArticle : (id) => httpRequest.get<SuccessResponse<Article>,any>(`/api/article/relatedArticle/${id}`),
}
export default articleApi;