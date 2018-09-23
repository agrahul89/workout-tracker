export class CategoryModel {

    constructor(
        public category: string,
        public editing: boolean = false,
        public id?: number
    ) { }

    startsWith(query: string): boolean {
        if (query) {
            return this.category.startsWith(query.trim());
        }
        return false;
    }

    matches(category: string): boolean {
        const query = category ? category.trim() : '';
        return this.category === query;
    }

    toString(): string {
        return JSON.stringify(this);
    }

}
