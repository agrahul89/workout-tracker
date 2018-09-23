import { Component, OnInit } from '@angular/core';
import { Subject } from 'rxjs';
import { debounceTime, distinctUntilChanged, map } from 'rxjs/operators';

import { CategoryModel } from './category-model';
import { AuthService } from '../_services/auth.service';
import { CategoryService } from '../_services/category.service';

@Component({
  selector: 'app-category',
  templateUrl: './category.component.html',
  styleUrls: ['./category.component.css']
})
export class CategoryComponent implements OnInit {

  private categories: CategoryModel[] = [];
  filteredCategories: CategoryModel[] = [];
  filterQuery$ = new Subject<string>();

  constructor(
    private authService: AuthService,
    private restService: CategoryService
  ) { }

  ngOnInit() {
    this.retrieveAll(this.authService.auth);
    this.filterQuery$.pipe(
      debounceTime(400), distinctUntilChanged(),
      map(filterQuery => this.filter(filterQuery))
    ).subscribe();
  }

  public add(category: string) {
    if (category) {
      this.restService.create(category, this.authService.auth).subscribe(
        res => {
          const resource = document.createElement('a');
          resource.href = res.headers.get('Location');
          const pathItems = resource.pathname.split('/');
          const id = pathItems[pathItems.length - 1];
          this.categories.push(new CategoryModel(category, false, parseInt(id, 10)));
          console.log(`Category [${category}] created successfully`);
        },
        err => {
          if (err.status === 401 || err.status === 403) {
            console.log('Unauthorized access to category');
          } else if (err.status === 400) {
            console.log('Non existing category');
          }
          alert('Error creating category');
        },
        () => {
          this.resetFilteredCategories();
        }
      );
    } else {
      console.log('Invalid category name :: ' + category);
    }
  }

  private delete(category: CategoryModel) {
    const confirmed = confirm('Deleting this category will also delete all workouts for this category');
    if (confirmed) {
      this.restService.delete(category.id, this.authService.auth).subscribe(
        res => {
          if (res.status === 200) {
            console.log(`Category #${category.id} deleted successfully`);
            console.log(`Category [${category.category}] deleted successfully`);
            this.categories.splice(this.categories.indexOf(category), 1);
          } else {
            console.log(`Category [#${category.category}] could not be deleted`);
          }
        },
        err => {
          if (err.status === 401 || err.status === 403) {
            console.log('Unauthorized access to category');
          } else if (err.status === 400) {
            console.log('Non existing category');
          }
          alert('Error deleting category');
        },
        () => this.resetFilteredCategories()
      );
    }
  }

  private edit(category: CategoryModel) {
    category.editing = true;
  }

  private filter(filterQuery: string): void {
    this.filteredCategories.length = 0;
    this.categories.forEach(
      (category: CategoryModel) => {
        const query = filterQuery ? filterQuery.trim().toLocaleLowerCase() : '';
        const categoryName = category.category ? category.category.trim().toLocaleLowerCase() : '';
        if (categoryName.startsWith(query)) {
          this.filteredCategories.push(category);
        }
      }
    );
  }

  private resetFilteredCategories() {
    this.filteredCategories.length = 0;
    this.categories.forEach(category => {
      this.filteredCategories.push(category);
    });
  }

  private retrieveAll(authToken: string): void {
    this.restService.getAll(authToken).subscribe(
      res => {
        if (res.status === 200 && res.body) {
          Array.from(res.body).forEach((category: CategoryModel) => {
            this.categories.push(CategoryService.clone(category));
          });
        } else if (res.status === 204) {
          console.log('No categories found');
          this.categories.length = 0;
        }
      },
      err => {
        if (err.status === 401 || err.status === 403) {
          console.log('Unauthorized access to category');
          this.categories.length = 0;
        }
        console.log('Error accessing categories');
      },
      () => this.resetFilteredCategories()
    );
  }

  private update(category: CategoryModel) {
    if (category && category.category && category.category.trim()) {
      this.restService.update(category, this.authService.auth).subscribe(
        res => {
          if (res.status === 200) {
            console.log(`Category #${category.id} updated successfully`);
            console.log(`Category updated successfully to [${category.category}]`);
            category.category = res.body.category;
          } else if (res.status === 204) {
            alert('Category is not available in database');
          } else {
            console.log(`Category could not be updated to [${category.category}]`);
          }
        },
        err => {
          if (err.status === 401 || err.status === 403) {
            console.log('Unauthorized access to category');
          }
          alert(`Error updating category [${category.id}]`);
        },
        () => {
          category.editing = false;
        }
      );
    }
  }

}
