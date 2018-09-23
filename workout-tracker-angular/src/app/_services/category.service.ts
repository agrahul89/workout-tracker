import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { ServiceBase } from '../_base/base-service';
import { CategoryModel } from '../category/category-model';

@Injectable({
  providedIn: 'root'
})
export class CategoryService extends ServiceBase {

  private categoryUrl: String = this.baseUrl + '/category';

  constructor(private client: HttpClient) {
    super(client);
  }

  static clone(category: CategoryModel): CategoryModel {
    return new CategoryModel(category.category, false, category.id);
  }

  create(category: string, authToken: string) {
    return this.client.post(
      this.categoryUrl.toString(),
      `{"category" : "${category}"}`,
      {
        headers: {
          'Content-Type' : 'application/json',
          'Accept' : 'application/json',
          'Authorization' : authToken
        },
        observe: 'response',
        reportProgress: false,
        responseType: 'json'
      }
    );
  }

  delete(id: number, authToken: string) {
    return this.client.delete(
      `${this.categoryUrl.toString()}/${id}`,
      {
        headers: {
          'Content-Type' : 'application/json',
          'Accept' : 'application/json',
          'Authorization' : authToken
        },
        observe: 'response',
        reportProgress: false,
        responseType: 'json'
      }
    );
  }

  getAll(authToken: string) {
    return this.client.get<CategoryModel[]>(
      this.categoryUrl.toString(),
      {
        headers: {
          'Accept' : 'application/json',
          'Authorization' : authToken
        },
        observe: 'response',
        reportProgress: false,
        responseType: 'json'
      }
    );
  }

  update(category: CategoryModel, authToken: string) {
    return this.client.put<CategoryModel>(
      `${this.categoryUrl.toString()}/${category.id}`,
      `{"id" : "${category.id}", "category" : "${category.category}"}`,
      {
        headers: {
          'Content-Type' : 'application/json',
          'Accept' : 'application/json',
          'Authorization' : authToken
        },
        observe: 'response',
        reportProgress: false,
        responseType: 'json'
      }
    );
  }

}
