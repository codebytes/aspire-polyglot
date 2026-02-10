import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RecipeService, Recipe } from './recipe.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <div class="container">
      <header>
        <h1>🍳 Recipe Manager</h1>
        <p>Manage your favorite recipes with CosmosDB</p>
      </header>

      <div class="controls">
        <input 
          type="text" 
          [(ngModel)]="searchQuery" 
          (ngModelChange)="onSearchChange()"
          placeholder="Search recipes or ingredients..."
          class="search-input"
        />
        
        <select [(ngModel)]="filterCategory" (ngModelChange)="loadRecipes()" class="category-select">
          <option value="">All Categories</option>
          <option value="Italian">Italian</option>
          <option value="Thai">Thai</option>
          <option value="Dessert">Dessert</option>
          <option value="Other">Other</option>
        </select>
        
        <button (click)="showAddForm = !showAddForm" class="btn-primary">
          {{ showAddForm ? 'Cancel' : '+ Add Recipe' }}
        </button>
      </div>

      <div *ngIf="showAddForm" class="recipe-form">
        <h2>{{ editingRecipe ? 'Edit Recipe' : 'Add New Recipe' }}</h2>
        <form (ngSubmit)="saveRecipe()">
          <div class="form-group">
            <label>Title</label>
            <input type="text" [(ngModel)]="currentRecipe.title" name="title" required class="form-control" />
          </div>
          
          <div class="form-group">
            <label>Description</label>
            <textarea [(ngModel)]="currentRecipe.description" name="description" required class="form-control"></textarea>
          </div>
          
          <div class="form-group">
            <label>Category</label>
            <select [(ngModel)]="currentRecipe.category" name="category" class="form-control">
              <option value="Italian">Italian</option>
              <option value="Thai">Thai</option>
              <option value="Dessert">Dessert</option>
              <option value="Other">Other</option>
            </select>
          </div>
          
          <div class="form-group">
            <label>Cook Time (minutes)</label>
            <input type="number" [(ngModel)]="currentRecipe.cookTimeMinutes" name="cookTime" required class="form-control" />
          </div>
          
          <div class="form-group">
            <label>Ingredients</label>
            <div *ngFor="let ingredient of currentRecipe.ingredients; let i = index" class="ingredient-row">
              <input type="text" [(ngModel)]="currentRecipe.ingredients[i]" [name]="'ingredient-' + i" class="form-control" />
              <button type="button" (click)="removeIngredient(i)" class="btn-remove">✕</button>
            </div>
            <button type="button" (click)="addIngredient()" class="btn-secondary">+ Add Ingredient</button>
          </div>
          
          <div class="form-group">
            <label>Instructions</label>
            <textarea [(ngModel)]="currentRecipe.instructions" name="instructions" required class="form-control" rows="5"></textarea>
          </div>
          
          <div class="form-actions">
            <button type="submit" class="btn-primary">{{ editingRecipe ? 'Update' : 'Create' }} Recipe</button>
            <button type="button" (click)="cancelEdit()" class="btn-secondary">Cancel</button>
          </div>
        </form>
      </div>

      <div class="recipes-grid">
        <div *ngFor="let recipe of filteredRecipes" class="recipe-card">
          <div class="recipe-header">
            <h3>{{ recipe.title }}</h3>
            <span class="category-badge">{{ recipe.category }}</span>
          </div>
          <p class="recipe-description">{{ recipe.description }}</p>
          <div class="recipe-meta">
            <span>⏱️ {{ recipe.cookTimeMinutes }} min</span>
            <span>🥘 {{ recipe.ingredients.length }} ingredients</span>
          </div>
          
          <div *ngIf="selectedRecipe?.id === recipe.id" class="recipe-details">
            <div class="detail-section">
              <h4>Ingredients:</h4>
              <ul>
                <li *ngFor="let ingredient of recipe.ingredients">{{ ingredient }}</li>
              </ul>
            </div>
            <div class="detail-section">
              <h4>Instructions:</h4>
              <p class="instructions">{{ recipe.instructions }}</p>
            </div>
          </div>
          
          <div class="recipe-actions">
            <button (click)="toggleDetails(recipe)" class="btn-secondary">
              {{ selectedRecipe?.id === recipe.id ? 'Hide Details' : 'View Details' }}
            </button>
            <button (click)="editRecipe(recipe)" class="btn-secondary">Edit</button>
            <button (click)="deleteRecipe(recipe.id!)" class="btn-danger">Delete</button>
          </div>
        </div>
      </div>

      <div *ngIf="filteredRecipes.length === 0" class="empty-state">
        <p>No recipes found. Add your first recipe to get started!</p>
      </div>
    </div>
  `,
  styles: [`
    .container {
      max-width: 1200px;
      margin: 0 auto;
      padding: 2rem;
    }

    header {
      text-align: center;
      margin-bottom: 2rem;
    }

    header h1 {
      font-size: 2.5rem;
      margin-bottom: 0.5rem;
      color: #2c3e50;
    }

    header p {
      color: #7f8c8d;
      font-size: 1.1rem;
    }

    .controls {
      display: flex;
      gap: 1rem;
      margin-bottom: 2rem;
      flex-wrap: wrap;
    }

    .search-input {
      flex: 1;
      min-width: 250px;
      padding: 0.75rem;
      border: 2px solid #e0e0e0;
      border-radius: 8px;
      font-size: 1rem;
    }

    .search-input:focus {
      outline: none;
      border-color: #ff6b6b;
    }

    .category-select {
      padding: 0.75rem;
      border: 2px solid #e0e0e0;
      border-radius: 8px;
      font-size: 1rem;
      background: white;
    }

    .btn-primary, .btn-secondary, .btn-danger, .btn-remove {
      padding: 0.75rem 1.5rem;
      border: none;
      border-radius: 8px;
      font-size: 1rem;
      cursor: pointer;
      transition: all 0.3s;
    }

    .btn-primary {
      background: #ff6b6b;
      color: white;
    }

    .btn-primary:hover {
      background: #ff5252;
    }

    .btn-secondary {
      background: #74b9ff;
      color: white;
    }

    .btn-secondary:hover {
      background: #0984e3;
    }

    .btn-danger {
      background: #fd79a8;
      color: white;
    }

    .btn-danger:hover {
      background: #e84393;
    }

    .btn-remove {
      background: #dfe6e9;
      color: #2d3436;
      padding: 0.5rem 1rem;
    }

    .recipe-form {
      background: white;
      padding: 2rem;
      border-radius: 12px;
      box-shadow: 0 4px 6px rgba(0,0,0,0.1);
      margin-bottom: 2rem;
    }

    .recipe-form h2 {
      margin-top: 0;
      color: #2c3e50;
    }

    .form-group {
      margin-bottom: 1.5rem;
    }

    .form-group label {
      display: block;
      margin-bottom: 0.5rem;
      font-weight: 600;
      color: #2c3e50;
    }

    .form-control {
      width: 100%;
      padding: 0.75rem;
      border: 2px solid #e0e0e0;
      border-radius: 8px;
      font-size: 1rem;
      box-sizing: border-box;
    }

    .form-control:focus {
      outline: none;
      border-color: #ff6b6b;
    }

    .ingredient-row {
      display: flex;
      gap: 0.5rem;
      margin-bottom: 0.5rem;
    }

    .ingredient-row input {
      flex: 1;
    }

    .form-actions {
      display: flex;
      gap: 1rem;
      margin-top: 1.5rem;
    }

    .recipes-grid {
      display: grid;
      grid-template-columns: repeat(auto-fill, minmax(350px, 1fr));
      gap: 1.5rem;
    }

    .recipe-card {
      background: white;
      padding: 1.5rem;
      border-radius: 12px;
      box-shadow: 0 4px 6px rgba(0,0,0,0.1);
      transition: transform 0.3s, box-shadow 0.3s;
    }

    .recipe-card:hover {
      transform: translateY(-4px);
      box-shadow: 0 8px 12px rgba(0,0,0,0.15);
    }

    .recipe-header {
      display: flex;
      justify-content: space-between;
      align-items: start;
      margin-bottom: 1rem;
    }

    .recipe-header h3 {
      margin: 0;
      color: #2c3e50;
      font-size: 1.3rem;
    }

    .category-badge {
      background: #74b9ff;
      color: white;
      padding: 0.25rem 0.75rem;
      border-radius: 20px;
      font-size: 0.85rem;
    }

    .recipe-description {
      color: #7f8c8d;
      margin-bottom: 1rem;
      line-height: 1.5;
    }

    .recipe-meta {
      display: flex;
      gap: 1rem;
      margin-bottom: 1rem;
      color: #95a5a6;
      font-size: 0.9rem;
    }

    .recipe-details {
      margin: 1rem 0;
      padding: 1rem;
      background: #f8f9fa;
      border-radius: 8px;
    }

    .detail-section {
      margin-bottom: 1rem;
    }

    .detail-section h4 {
      margin-top: 0;
      color: #2c3e50;
    }

    .detail-section ul {
      margin: 0;
      padding-left: 1.5rem;
    }

    .detail-section li {
      margin-bottom: 0.5rem;
      color: #555;
    }

    .instructions {
      color: #555;
      line-height: 1.6;
      white-space: pre-line;
    }

    .recipe-actions {
      display: flex;
      gap: 0.5rem;
      flex-wrap: wrap;
    }

    .recipe-actions button {
      flex: 1;
      min-width: 80px;
    }

    .empty-state {
      text-align: center;
      padding: 3rem;
      color: #95a5a6;
      font-size: 1.2rem;
    }
  `]
})
export class AppComponent implements OnInit {
  recipes: Recipe[] = [];
  filteredRecipes: Recipe[] = [];
  selectedRecipe: Recipe | null = null;
  showAddForm = false;
  editingRecipe = false;
  searchQuery = '';
  filterCategory = '';
  
  currentRecipe: Recipe = this.getEmptyRecipe();

  constructor(private recipeService: RecipeService) {}

  ngOnInit() {
    this.loadRecipes();
  }

  loadRecipes() {
    this.recipeService.getRecipes().subscribe(recipes => {
      this.recipes = recipes;
      this.applyFilters();
    });
  }

  applyFilters() {
    this.filteredRecipes = this.recipes.filter(recipe => {
      const matchesCategory = !this.filterCategory || recipe.category === this.filterCategory;
      const matchesSearch = !this.searchQuery || 
        recipe.title.toLowerCase().includes(this.searchQuery.toLowerCase()) ||
        recipe.ingredients.some(i => i.toLowerCase().includes(this.searchQuery.toLowerCase()));
      return matchesCategory && matchesSearch;
    });
  }

  onSearchChange() {
    if (this.searchQuery.trim()) {
      this.recipeService.searchRecipes(this.searchQuery).subscribe(recipes => {
        this.recipes = recipes;
        this.applyFilters();
      });
    } else {
      this.loadRecipes();
    }
  }

  toggleDetails(recipe: Recipe) {
    this.selectedRecipe = this.selectedRecipe?.id === recipe.id ? null : recipe;
  }

  saveRecipe() {
    if (this.editingRecipe && this.currentRecipe.id) {
      this.recipeService.updateRecipe(this.currentRecipe.id, this.currentRecipe).subscribe(() => {
        this.loadRecipes();
        this.cancelEdit();
      });
    } else {
      this.recipeService.createRecipe(this.currentRecipe).subscribe(() => {
        this.loadRecipes();
        this.cancelEdit();
      });
    }
  }

  editRecipe(recipe: Recipe) {
    this.currentRecipe = { ...recipe, ingredients: [...recipe.ingredients] };
    this.editingRecipe = true;
    this.showAddForm = true;
    window.scrollTo({ top: 0, behavior: 'smooth' });
  }

  deleteRecipe(id: string) {
    if (confirm('Are you sure you want to delete this recipe?')) {
      this.recipeService.deleteRecipe(id).subscribe(() => {
        this.loadRecipes();
        if (this.selectedRecipe?.id === id) {
          this.selectedRecipe = null;
        }
      });
    }
  }

  cancelEdit() {
    this.currentRecipe = this.getEmptyRecipe();
    this.editingRecipe = false;
    this.showAddForm = false;
  }

  addIngredient() {
    this.currentRecipe.ingredients.push('');
  }

  removeIngredient(index: number) {
    this.currentRecipe.ingredients.splice(index, 1);
  }

  getEmptyRecipe(): Recipe {
    return {
      title: '',
      description: '',
      ingredients: [''],
      instructions: '',
      cookTimeMinutes: 30,
      category: 'Other'
    };
  }
}
