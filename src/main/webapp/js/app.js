let app = Vue.createApp(
  {
    data() {
      return {
        isLoading: true,
        isEditMode: false,
        showFilter: false,
        isPasswordVisible: false,
        userRole: 'Manager',
        username: '',
        userPassword: '',
        errorMassage: '',

        productsCategories: [],
        products: [],
        customers: [],
        employees: [],
        checks: [],

        selectedFilter: '',
        filtersApplied: false,

        newCategory: {
          category_name: '',
          category_number: null
        },

        currentCategory: {
          category_name: ''
        },


        newProduct: {
          UPC: null,
          selling_price: null,
          image: '',
          product: {
            category: {
              category_number: null,
              category_name: ''
            },
            product_name: '',
            description: '',
          },
          products_number: 0,
          new_price: null,
        },
        imagePreview: null,
        currentProduct: null,
        productStatus: null,
        totalPieces: 0,
        isUPCFiltered: false,
        sortProductsParamsField: [],
        productTypeFilter: null,

        newCustomer: {
          card_number: null,
          cust_surname: '',
          cust_name: '',
          cust_patronymic: '',
          phone_number: '',
          city: '',
          street: '',
          zip_code: '',
          percent: 0
        },
        currentCustomer: null,

        currentCheck: null,
        newCheck: {
          check_number: null,
          print_date: null,
          id_employee: null,
          card_number: null,
          sum_total: 0,
          vat: 0,
          sales: []
        },
        totalSum: 0,
        showTotalSumChecked: false,

        newEmployee: {
          id_employee: null,
          empl_surname: '',
          empl_name: '',
          empl_patronymic: '',
          empl_role: '',
          salary: 0,
          date_of_birth: '',
          date_of_start: '',
          phone_number: '+',
          city: '',
          street: '',
          zip_code: '',
          is_active: true
        },
        currentEmployee: null,

        currentEditingItemID: null,

        productsItemsToShowCount: 15,
        productsItemsIncrementBy: 15,
        rowsToShowCount: 10,
        rowsIncrementBy: 10,
      }
    },
    computed: {
      isManager() {
        return this.userRole === "Manager"
      },
      isCashier() {
        return this.userRole === "Cashier"
      },
      statusClass() {
        const product = this.currentProduct || this.newProduct
        return {
          'in-stock': product?.products_number > 0,
          'out-of-stock': !product?.products_number || product?.products_number <= 0
        }
      },
      subtotal() {
        return this.newCheck.sales.reduce((total, sale) => {
          return total + (sale.selling_price * sale.quantity)
        }, 0)
      },
      discountPercent() {
        return this.currentCustomer?.percent || 0
      },
      totalAfterDiscount() {
        const discount = this.discountPercent / 100
        return this.subtotal * (1 - discount)
      },
      vatAmount() {
        return this.totalAfterDiscount * 0.2
      },
    },
    watch: {
      currentProduct(newVal) {
        if (newVal) {
          document.title = `${newVal.product.product_name} - Zlagoda`
        }
      },
      currentCustomer(newVal) {
        if (newVal) {
          const patronymic = newVal.cust_patronymic ? ` ${newVal.cust_patronymic}` : ''
          document.title = `${newVal.cust_surname} ${newVal.cust_name} ${patronymic} - Zlagoda`
        }
      },
      currentEmployee(newVal) {
        if (newVal) {
          const patronymic = newVal.empl_patronymic ? ` ${newVal.empl_patronymic}` : ''
          document.title = `${newVal.empl_surname} ${newVal.empl_name} ${patronymic} - Zlagoda`
        }
      },
      currentCheck(newVal) {
        if (newVal) {
          document.title = `Check ${newVal.check_number} - Zlagoda`
        }
      },
      'currentProduct.products_number': function (newCount) {
        if (this.currentProduct) {
          if (newCount <= 0) {
            this.currentProduct.status = 'Out Of Stock'
          } else if (this.currentProduct.status === 'Out Of Stock') {
            this.currentProduct.status = 'In Stock'
          }
        }
      },
      'newProduct.products_number': function (newCount) {
        if (this.newProduct) {
          if (newCount <= 0) {
            this.newProduct.status = 'Out Of Stock'
          } else if (this.newProduct.status === 'Out Of Stock') {
            this.newProduct.status = 'In Stock'
          }
        }
      }
    },
    methods: {
      async handleLogin() {
        const payload = {
          username: this.username,
          password: this.userPassword
        }

        try {
          const response = await fetch('http://localhost:8090/auth/login', {
            method: 'POST',
            headers: {
              'Content-Type': 'application/json'
            },
            body: JSON.stringify(payload)
          })

          const data = await response.json()

          if (!response.ok) {
            const status = response.status

            if (status === 401) {
              this.errorMessage = data.message || 'Incorrect username or password'
            } else if (status === 404) {
              this.errorMessage = data.message || 'Username not found'
            } else if (status === 400) {
              this.errorMessage = data.message || 'Invalid login request'
            } else {
              this.errorMessage = data.message || 'An unknown error occurred'
            }
          }
          else {
            console.log('Login successful')
            this.errorMessage = ''
            window.location.href = 'categories.html'
          }
        } catch (error) {
          this.errorMessage = 'Unexpected error during login'
          console.error('Login error:', error)
        }
      },

      displayedItems(listName) {
        return (array) => {
          if (!array || !Array.isArray(array)) {
            console.error(`Invalid array passed to displayedItems method: ${listName}`)
            return []
          }
          if (listName == 'products') {
            return array.slice(0, this.productsItemsToShowCount)
          }
          return array.slice(0, this.rowsToShowCount)
        }
      },
      hasMoreItems(listName) {
        return (array) => {
          if (!array || !Array.isArray(array)) {
            return false
          }
          if (listName == 'products') {
            return this.productsItemsToShowCount < array.length
          }
          return this.rowsToShowCount < array.length
        }
      },
      showMoreItems(listName) {
        if (listName == 'products') {
          this.productsItemsToShowCount += this.productsItemsIncrementBy
          return
        }
        this.rowsToShowCount += this.rowsIncrementBy
      },

      async getProductById(id) {
        try {
          const response = await fetch(`http://localhost:8090/product/${id}`, {
            method: 'GET',
            headers: {
              'Content-Type': 'application/json',
            }
          })

          if (!response.ok) {
            throw new Error(`Failed to fetch product. Status: ${response.status}`)
          }

          const product = await response.json()
          return product

        } catch (error) {
          console.error("An error occurred during fetching product:", error)
          alert("An unexpected error occurred. Please try again later.")
          return null
        }
      },
      async getEmployeeById(id) {
        // try {
        //   const response = await fetch('/api/employees/get', {
        //     method: 'POST',
        //     headers: {
        //       'Content-Type': 'application/json',
        //     },
        //     body: JSON.stringify({ employeeId: id }),
        //   })

        //   if (!response.ok) {
        //     throw new Error(`Failed to fetch employee. Status: ${response.status}`)
        //   }
        //   const employee = await response.json()
        //   return employee

        // } catch (error) {
        //   console.error("An error occurred during fetching employee:", error)
        //   alert("An unexpected error occurred. Please try again later.")
        //   return null
        // }
        return this.employees.find(employee => employee.id_employee === id)
      },
      async getCustomerById(id) {
        // try {
        //   const response = await fetch('/api/customers/get', {
        //     method: 'POST',
        //     headers: {
        //       'Content-Type': 'application/json',
        //     },
        //     body: JSON.stringify({ customerId: id }),
        //   })

        //   if (!response.ok) {
        //     throw new Error(`Failed to fetch customer. Status: ${response.status}`)
        //   }
        //   const customer = await response.json()
        //   return customer

        // } catch (error) {
        //   console.error("An error occurred during fetching customer:", error)
        //   alert("An unexpected error occurred. Please try again later.")
        //   return null
        // }
        return this.customers.find(customer => customer.card_number === id)
      },
      async getCheckById(id) {
        // try {
        //   const response = await fetch('/api/checks/get', {
        //     method: 'POST',
        //     headers: {
        //       'Content-Type': 'application/json',
        //     },
        //     body: JSON.stringify({ check_number: id }),
        //   })

        //   if (!response.ok) {
        //     throw new Error(`Failed to fetch check. Status: ${response.status}`)
        //   }
        //   const check = await response.json()
        //   return check

        // } catch (error) {
        //   console.error("An error occurred during fetching check:", error)
        //   alert("An unexpected error occurred. Please try again later.")
        //   return null
        // }
        return this.checks.find(check => check.check_number === id)
      },

      async loadDataForCurrentPage() {
        const path = window.location.pathname
        const page = path.substring(path.lastIndexOf('/') + 1)

        const idFromURL = () => new URLSearchParams(window.location.search).get('id')
        this.isLoading = true
        try {
          switch (page) {
            case 'categories.html':
              await this.loadCategories()
              break

            case 'products.html':
              await Promise.all([this.loadProducts(), this.loadCategories()])
              break

            case 'product-page.html':
              await Promise.all([this.loadProducts(), this.loadCategories()])
              const productId = idFromURL()
              if (productId) {
                this.currentProduct = await this.getProductById(productId)
              }
              break

            case 'new-product-page.html':
              await this.loadCategories()
              break

            case 'customers.html':
              await this.loadCustomers()
              break

            case 'customer-page.html':
              await this.loadCustomers()
              const customerId = idFromURL()
              if (customerId) {
                this.currentCustomer = await this.getCustomerById(customerId)
              }
              break

            case 'checks.html':
              await Promise.all([
                this.loadChecks(),
                this.loadEmployees()
              ])
              break

            case 'check-page.html':
              await Promise.all([
                this.loadChecks(),
                this.loadProducts(),
                this.loadEmployees(),
                this.loadCustomers(),
              ])
              const checkId = idFromURL()
              if (checkId) {
                const check = await this.getCheckById(checkId)
                this.currentCheck = check

                const [employee, customer] = await Promise.all([
                  this.getEmployeeById(check.id_employee),
                  this.getCustomerById(check.card_number),
                ])

                this.currentCheck.employeeName = `${employee.empl_surname} ${employee.empl_name} ${employee.empl_patronymic || ''}`
                this.currentCheck.customerName = `${customer.cust_surname} ${customer.cust_name} ${customer.cust_patronymic || ''}`
              }
              break

            case 'new-check-page.html':
              await Promise.all([
                this.loadProducts(),
                this.loadEmployees(),
                this.loadCustomers(),
              ])
              break

            case 'employees.html':
              await this.loadEmployees()
              break

            case 'employee-page.html':
              await this.loadEmployees()
              const employeeId = idFromURL()
              if (employeeId) {
                this.currentEmployee = await this.getEmployeeById(employeeId)
              }
              break

            default:
              console.warn('No data loading defined for page:', page)
          }
        } catch (error) {
          console.error('Error loading data for page:', page, error)
        } finally {
          this.isLoading = false
        }
      },
      async loadCategories() {
        try {
          const response = await fetch("http://localhost:8090/category")

          if (!response.ok) throw new Error("Fetch categories error! Status: ${response.status}")

          this.productsCategories = await response.json()
        } catch (error) {
          console.error("Could not load categories:", error)
        }
      },
      async loadProducts() {
        try {
          const response = await fetch("http://localhost:8090/product")
          if (!response.ok) throw new Error("Fetch products error: ${response.status}")

          this.products = await response.json()
        } catch (error) {
          console.error("Error loading product:", error)
        }
      },
      async loadProductsByCategory(category_name) {
        this.isLoading = true
        try {
          const response = await fetch(`http://localhost:8090/product/by-category/${category_name}`)
          if (response.ok) {
            this.products = await response.json()
          } else {
            console.error("Failed to load products. Status.", response.status)
          }
        } catch (error) {
          console.error("Error loading products by category.", error)
        } finally {
          this.isLoading = false
        }
      },
      async loadCustomers() {
        try {
          const response = await fetch("http://localhost:8090/customer")
          if (!response.ok) throw new Error("Fetch customers error! Status: ${response.status}")

          this.customers = await response.json()
        } catch (error) {
          console.error("Could not load customers:", error)
        }
      },
      async loadChecks() {
        try {
          const response = await fetch("../checks.json")

          if (!response.ok) throw new Error("Fetch checks error! Status: ${response.status}")

          this.checks = await response.json()
        } catch (error) {
          console.error("Could not load checks:", error)
        }
      },
      async loadEmployees() {
        try {
          const response = await fetch("http://localhost:8090/employee")

          if (!response.ok) throw new Error("Fetch employees error! Status: ${response.status}")

          this.employees = await response.json()
        } catch (error) {
          console.error("Could not load employees:", error)
        }
      },

      togglePasswordVisibility() {
        this.isPasswordVisible = !this.isPasswordVisible
      },
      toggleEditMode() {
        this.isEditMode = !this.isEditMode
      },
      toggleFilterShow() {
        this.showFilter = !this.showFilter
      },
      toggleEdit(itemId) {
        this.currentEditingItemID = this.currentEditingItemID === itemId ? null : itemId
      },

      async confirmDeleteCategory(categoryId) {
        const categoryIdToDelete = categoryId
        if (confirm("Are you sure you want to delete this category?")) {
          try {
            const response = await fetch(`http://localhost:8090/category/${categoryId}`, {
              method: 'DELETE',
            })

            if (response.ok) {
              this.productsCategories = this.productsCategories.filter(item => item.id !== categoryIdToDelete)
              this.newCategory = { category_name: '', category_number: null }
              await this.loadCategories()
            } else {
              console.error("Deletion failed on the server. Status:", response.status)
              alert("Failed to delete category. Please try again.")
            }
          } catch (error) {
            console.error("An error occurred during deletion:", error)
            alert("An unexpected error occurred. Please try again later.")
          }
        } else {
          console.log("Deletion cancelled by user.")
        }
      },
      async addNewCategory() {
        try {
          const response = await fetch('http://localhost:8090/category', {
            method: 'POST',
            headers: {
              'Content-Type': 'application/json',
            },
            body: JSON.stringify(this.newCategory),
          })

          if (response.ok) {
            const addedCategory = { ...this.newCategory }
            this.productsCategories.push(addedCategory)
            console.log("New product added successfully:", addedCategory)
            this.newCategory = { category_name: '', category_number: null }
            this.loadCategories()
          }
          else {
            console.error("Adding category failed on the server. Status:", response.status)
            alert("Failed to add category. Please try again.")
          }
        } catch (error) {
          console.error("An unexpected error occurred during adding:", error)
          alert("An unexpected error occurred. Please try again later.")
        }
      },
      async saveEditCategory(category) {
        try {
          const response = await fetch(`http://localhost:8090/category`, {
            method: 'PUT',
            headers: {
              'Content-Type': 'application/json',
            },
            body: JSON.stringify(category),
          })

          if (response.ok) {
            this.currentEditingItemID = null
          } else {
            console.error("Updating category failed on the server. Status:", response.status)
            alert("Failed to update category. Please try again.")
          }
        } catch (error) {
          console.error("An unexpected error occurred during updating:", error)
          alert("An unexpected error occurred. Please try again later.")
        }
      },
      async sortCategories() {
        try {
          this.isLoading = true
          const response = await fetch('http://localhost:8090/category/filter')
          if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`)
          }
          this.productsCategories = await response.json()
        } catch (error) {
          console.error('Error sorting categories:', error)
          alert('Failed to sort categories. Please try again.')
        } finally {
          this.isLoading = false
        }
      },

      handlePriceInput(event, isPromotional) {
        const value = parseFloat(event.target.value) || 0
        if (isPromotional) {
          this.currentProduct.new_price = value
        } else {
          this.currentProduct.selling_price = value
        }
      },
      handleImageUpload(event) {
        const file = event.target.files[0]
        this.selectedFile = file

        if (file) {
          this.imagePreview = URL.createObjectURL(file)
        }
      },
      goToAddProduct() {
        window.location.href = 'new-product-page.html'
      },
      async confirmAndDeleteProduct() {
        const productId = this.currentProduct.id
        if (confirm("Are you sure you want to delete this product?")) {
          try {
            const response = await fetch(`http://localhost:8090/product/${productId}`, {
              method: 'DELETE',
            })

            if (response.ok) {
              this.products = this.products.filter(item => item.id !== productId)
              this.currentProduct = null
              window.location.href = 'products.html'
            } else {
              console.error("Deletion failed on the server. Status:", response.status)
              alert("Failed to delete product. Please try again.")
            }
          } catch (error) {
            console.error("An error occurred during deletion:", error)
            alert("An unexpected error occurred. Please try again later.")
          }
        } else {
          console.log("Deletion cancelled by user.")
        }
      },
      async addNewProduct() {
        try {
          const response = await fetch('http://localhost:8090/product', {
            method: 'POST',
            headers: {
              'Content-Type': 'application/json',
            },
            body: JSON.stringify(this.newProduct),
          })

          if (response.ok) {
            const newProduct = await response.json()
            this.products.push(newProduct)
            this.newProduct = {
              id: null,
              name: '',
              price: null,
              image: '',
              category: '',
              description: '',
              manufacturer: '',
              status: 'Out Of Stock',
              count: 0
            }
            console.log("New product added successfully:")
            window.location.href = `products.html`
          } else {
            console.error("Adding product failed on the server. Status:", response.status)
            alert("Failed to add product. Please try again.")
          }
        } catch (error) {
          console.error("An unexpected error occurred during adding:", error)
          alert("An unexpected error occurred. Please try again later.")
        }
      },
      async saveEditProduct() {
        try {
          const response = await fetch(`http://localhost:8090/product/${this.currentProduct.UPC}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(this.currentProduct),
          })

          if (response.ok) {
            this.currentEditingItemID = null
            window.location.href = "products.html"
          } else {
            console.error("Updating product failed on the server. Status:", response.status)
            alert("Failed to update product. Please try again.")
          }
        } catch (error) {
          console.error("An unexpected error occurred during updating:", error)
          alert("An unexpected error occurred. Please try again later.")
        }
      },
      async applyProductFilters() {
  try {
        this.isLoading = true;

        const categorySelect = document.getElementById('category-select');
        const fromDateInput = document.getElementById('from-date');
        const toDateInput = document.getElementById('to-date');

        let categoryName = null;
        const categoryNumber = categorySelect ? categorySelect.value : null;
        if (categorySelect && categorySelect.options && categorySelect.selectedIndex !== -1) {
          categoryName = categorySelect.options[categorySelect.selectedIndex].dataset.name;
        }

        const fromDate = fromDateInput?.value || null;
        const toDate = toDateInput?.value || null;
        const productType = this.productTypeFilter;

        const params = new URLSearchParams();

        if (categoryNumber) params.append('category', categoryName);
        if (fromDate) params.append('from_date', fromDate);
        if (toDate) params.append('to_date', toDate);
        if (productType === 'promotional') params.append('promotional', true);
        else if (productType === 'non-promotional') params.append('promotional', false);

        if (this.sortProductsParamsField?.length > 0) {
          this.sortProductsParamsField.forEach(field => {
            params.append('sortBy', field); // якщо бекенд очікує ?sort=product_name&sort=products_number
          });
        }

        const response = await fetch(`http://localhost:8090/product/filter?${params.toString()}`, {
          method: 'GET',
          headers: {
            'Content-Type': 'application/json'
          }
        });

        if (!response.ok) {
          throw new Error(`Failed to filter products. Status: ${response.status}`);
        }

        const data = await response.json();
        this.products = data;
        this.totalPieces = data.total_pieces || 0;
        this.filtersApplied = true;
        this.currentCategory = { category_name: categoryName };
      } catch (error) {
        console.error('Error applying filters to products:', error);
        alert('Failed to apply filters to products. Please try again.');
      } finally {
        this.isLoading = false;
      }
    },

      clearProductFilters() {
        const categorySelect = document.getElementById('category-select')
        const fromDateInput = document.getElementById('from-date')
        const toDateInput = document.getElementById('to-date')
        const showPromotionalCheckbox = document.getElementById('show-promotional')
        const showNonPromotionalCheckbox = document.getElementById('show-non-promotional')

        if (categorySelect) categorySelect.value = ''
        if (fromDateInput) fromDateInput.value = ''
        if (toDateInput) toDateInput.value = ''
        if (showPromotionalCheckbox) showPromotionalCheckbox.checked = false
        if (showNonPromotionalCheckbox) showNonPromotionalCheckbox.checked = false
        this.sortProductsParamsField = null

        this.loadProducts()
        this.filtersApplied = false
        this.totalPieces = 0
        this.currentProduct = null
        this.isUPCFiltered = false
      },
      async applyProductSort() {
        try {
          if (!this.sortProductsParamsField) {
            return
          }

          const params = new URLSearchParams()
          params.append('sort_by', this.sortParams.field)
          params.append('order', this.sortParams.direction)

          const response = await fetch(`http://localhost:8090/product/sorted?${params.toString()}`, {
            method: 'GET',
            headers: {
              'Content-Type': 'application/json'
            }
          })

          if (!response.ok) {
            throw new Error('Failed to fetch sorted products')
          }

          this.isLoading = true
          this.products = await response.json()
          this.showSort = false

        } catch (error) {
          console.error('Sorting error:', error)
          alert('Failed to apply sorting. Please try again.')
        } finally {
          this.isLoading = false
        }
      },

      goToAddCustomer() {
        window.location.href = 'new-customer-page.html'
      },
      async addNewCustomer() {
        try {
          const response = await fetch('http://localhost:8090/customer', {
            method: 'POST',
            headers: {
              'Content-Type': 'application/json',
            },
            body: JSON.stringify(this.newCustomer),
          })

          if (response.ok) {
            const newCustomer = await response.json()
            this.customers.push(newCustomer)
            console.log("New customer added successfully:", newCustomer)
            window.location.href = `customers.html`
          } else {
            console.error("Adding customer failed on the server. Status:", response.status)
            alert("Failed to add customer. Please try again.")
          }
        } catch (error) {
          console.error("An unexpected error occurred during adding:", error)
          alert("An unexpected error occurred. Please try again later.")
        }
      },
      async confirmAndDeleteCustomer() {
        const customerId = this.currentCustomer.card_number
        if (confirm("Are you sure you want to delete this customer?")) {
          try {
            const response = await fetch(`http://localhost:8090/customer/${customerId}`, {
              method: 'DELETE',
            })

            if (response.ok) {
              this.customers = this.customers.filter(item => item.card_number !== customerId)
              this.currentCustomer = null
              window.location.href = 'customers.html'
              this.loadCustomers()
            } else {
              console.error("Deletion failed on the server. Status:", response.status)
              alert("Failed to delete customer. Please try again.")
            }
          } catch (error) {
            console.error("An error occurred during deletion:", error)
            alert("An unexpected error occurred. Please try again later.")
          }
        } else {
          console.log("Deletion cancelled by user.")
        }
      },
      async saveEditCustomer() {
        try {
          const response = await fetch(`http://localhost:8090/customer`, {
            method: 'PUT',
            headers: {
              'Content-Type': 'application/json',
            },
            body: JSON.stringify(this.currentCustomer),
          })

          if (response.ok) {
            this.currentCustomer = null
            window.location.href = "customers.html"
          } else {
            console.error("Updating customer failed on the server. Status:", response.status)
            alert("Failed to update customer. Please try again.")
          }
        } catch (error) {
          console.error("An unexpected error occurred during updating:", error)
          alert("An unexpected error occurred. Please try again later.")
        }
      },
      formatCustomerName(customer) {
        return `${customer.cust_surname} ${customer.cust_name} ${customer.cust_patronymic || ''}`
      },
      async applyCustomerFilters() {
        try {
          this.isLoading = true

          const discountSelect = document.getElementById('discount-select')
          const discountPercent = discountSelect ? discountSelect.value : null
          const sortNameCheckbox = document.getElementById('sort-name')
          const sortByName = sortNameCheckbox ? sortNameCheckbox.checked : false

          const params = new URLSearchParams()

          if (discountPercent) params.append('percent', discountPercent)

          if (sortByName) params.append('sort', sortByName)

          if (params.size > 0) {
            const response = await fetch(`http://localhost:8090/customers/filter?${params.toString()}`, {
              method: 'GET',
              headers: {
                'Content-Type': 'application/json'
              }
            })

            if (!response.ok) {
              throw new Error(`Failed to filter customers. Status: ${response.status}`)
            }

            this.customers = await response.json()
            this.filtersApplied = true
          } else {
            await this.loadCustomers()
            this.filtersApplied = false
          }
        } catch (error) {
          console.error('Error applying filters to customers:', error)
          alert('Failed to apply filters to customers. Please try again.')
        } finally {
          this.isLoading = false
        }
      },
      clearCustomerFilters() {
        const discountSelect = document.getElementById('discount-select')
        const sortNameCheckbox = document.getElementById('sort-name')

        if (discountSelect) discountSelect.value = ''
        if (sortNameCheckbox) sortNameCheckbox.checked = false

        this.loadCustomers()
        this.filtersApplied = false
      },

      async sortCustomers() {
        try {
          this.isLoading = true
          const response = await fetch('http://localhost:8090/customers?sort=surname')
          if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`)
          }
          this.customers = await response.json()
        } catch (error) {
          console.error('Error sorting customers:', error)
          alert('Failed to sort customers. Please try again.')
        } finally {
          this.isLoading = false
        }
      },


      goToAddCheck() {
        window.location.href = 'new-check-page.html'
      },
      async addNewCheck() {
        try {
          const response = await fetch('...', {
            method: 'POST',
            headers: {
              'Content-Type': 'application/json',
            },
            body: JSON.stringify(this.newCheck),
          })

          if (response.ok) {
            const newCheck = await response.json()
            this.checks.push(newCheck)
            console.log("New check added successfully:", newCheck)
            window.location.href = `check-page.html?id=${newCheck.check_number}`
          } else {
            console.error("Adding check failed on the server. Status:", response.status)
            alert("Failed to add check. Please try again.")
          }
        } catch (error) {
          console.error("An unexpected error occurred during adding:", error)
          alert("An unexpected error occurred. Please try again later.")
        }
      },
      addSale() {
        this.newCheck.sales.push({
          product_id: '',
          product_name: '',
          selling_price: 0,
          quantity: 1
        })
      },
      removeSale(index) {
        this.newCheck.sales.splice(index, 1)
      },
      async onProductSelected(index) {
        const productId = this.newCheck.sales[index].product_id

        try {
          const selectedProduct = await this.getProductById(productId)

          if (selectedProduct) {
            this.newCheck.sales[index].selling_price = selectedProduct.selling_price
            this.newCheck.sales[index].product_name = selectedProduct.product.product_name
          } else {
            this.newCheck.sales[index].selling_price = 0
            this.newCheck.sales[index].product_name = ''
          }
        } catch (error) {
          console.error("Error fetching product:", error)
          this.newCheck.sales[index].selling_price = 0
          this.newCheck.sales[index].product_name = ''
        }
      },
      async onCustomerSelected() {
        const customerId = this.newCheck.card_number
        if (customerId) {
          await this.getCustomerById(customerId)
            .then(customer => {
              this.currentCustomer = customer
            })
            .catch(error => {
              console.error("Error fetching customer:", error)
              this.currentCustomer = null
            })
        } else {
          this.currentCustomer = null
        }
      },
      async applyCheckFilters() {
        try {
          const cashierSelect = document.getElementById('cashier-select')
          const fromDateInput = document.getElementById('from-date')
          const toDateInput = document.getElementById('to-date')
          const showTotalSumCheckbox = document.getElementById('show-total-sum')
          const sortCheckNumberCheckbox = document.getElementById('sort-check-number')

          let cashierName = null
          if (cashierSelect && cashierSelect.options && cashierSelect.selectedIndex !== -1) {
            cashierName = cashierSelect.options[cashierSelect.selectedIndex].dataset.name
          }

          const fromDate = fromDateInput ? fromDateInput.value : null
          const toDate = toDateInput ? toDateInput.value : null
          const showTotalSum = showTotalSumCheckbox ? showTotalSumCheckbox.checked : false
          const sortByCheckNumber = sortCheckNumberCheckbox ? sortCheckNumberCheckbox.checked : false

          this.showTotalSumChecked = showTotalSum

          const params = new URLSearchParams()

          if (cashierName) params.append('empl_name', cashierName)
          if (fromDate) params.append('from_date', fromDate)
          if (toDate) params.append('to_date', toDate)
          if (showTotalSum) params.append('show_total_sum', showTotalSum)
          if (sortByCheckNumber) params.append('sort', sortByCheckNumber)

          if (params.size > 0) {
            const response = await fetch(`http://localhost:8090/checks/filter?${params.toString()}`, {
              method: 'GET',
              headers: {
                'Content-Type': 'application/json'
              }
            })

            if (!response.ok) {
              throw new Error(`Failed to filter checks. Status: ${response.status}`)
            }
            this.isLoading = true
            const data = await response.json()
            this.checks = data.checks
            this.filtersApplied = true
          } else {
            await this.loadChecks()
            this.filtersApplied = false
          }
        } catch (error) {
          console.error('Error applying filters to checks:', error)
          alert('Failed to apply filters to checks. Please try again.')
        } finally {
          this.isLoading = false
        }
      },
      clearCheckFilters() {
        const cashierSelect = document.getElementById('cashier-select')
        const fromDateInput = document.getElementById('from-date')
        const toDateInput = document.getElementById('to-date')
        const showTotalSumCheckbox = document.getElementById('show-total-sum')
        const sortCheckNumberCheckbox = document.getElementById('sort-check-number')

        if (cashierSelect) cashierSelect.value = ''
        if (fromDateInput) fromDateInput.value = ''
        if (toDateInput) toDateInput.value = ''
        if (showTotalSumCheckbox) showTotalSumCheckbox.checked = false
        this.showTotalSumChecked = false
        if (sortCheckNumberCheckbox) sortCheckNumberCheckbox.checked = false

        this.loadChecks()
        this.filtersApplied = false
      },

      goToAddEmployee() {
        window.location.href = 'new-employee-page.html'
      },
      async addNewEmployee() {
        try {
          const response = await fetch('http://localhost:8090/employee', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(this.newEmployee),
          })

          if (response.ok) {
            window.location.href = `employees.html`
          } else {
            alert("Failed to add employee. Please try again.")
          }
        } catch (error) {
          console.error("An unexpected error occurred during adding:", error)
          alert("An unexpected error occurred. Please try again later.")
        }
      },
      async confirmAndDeleteEmployee() {
        const employeeId = this.currentEmployee.id_employee
        if (confirm("Are you sure you want to delete this employee?")) {
          try {
            const response = await fetch(`http://localhost:8090/employee/${employeeId}`, {
              method: 'DELETE',
            })

            if (response.ok) {
              this.employees = this.employees.filter(item => item.id_employee !== employeeId)
              this.currentEmployee = null
              window.location.href = 'employees.html'
            } else {
              console.error("Deletion failed on the server. Status:", response.status)
              alert("Failed to delete employee. Please try again.")
            }
          } catch (error) {
            console.error("An unexpected error occurred during deletion:", error)
            alert("An unexpected error occurred. Please try again later.")
          }
        } else {
          console.log("Deletion cancelled by user.")
        }
      },
      async saveEditEmployee() {
        try {
          const response = await fetch(`http://localhost:8090/employee/${this.currentEmployee.id_employee}`, {
            method: 'PUT',
            headers: {
              'Content-Type': 'application/json',
            },
            body: JSON.stringify(this.currentEmployee),
          })

          if (response.ok) {
            this.currentEmployee = null
            window.location.href = "employees.html"
          } else {
            console.error("Updating employee failed on the server. Status:", response.status)
            alert("Failed to update employee. Please try again.")
          }
        } catch (error) {
          console.error("An unexpected error occurred during updating:", error)
          alert("An unexpected error occurred. Please try again later.")
        }
      },
      formatEmployeeName(employee) {
        return `${employee.empl_surname} ${employee.empl_name} ${employee.empl_patronymic || ''}`
      },
      async applyEmployeeFilters() {
        try {
          this.isLoading = true

          const showCashiersCheckbox = document.getElementById('show-cashiers')
          const showManagersCheckbox = document.getElementById('show-managers')
          const sortSurnameCheckbox = document.getElementById('sort-surname')

          const showCashiers = showCashiersCheckbox ? showCashiersCheckbox.checked : false
          const showManagers = showManagersCheckbox ? showManagersCheckbox.checked : false
          const sortBySurname = sortSurnameCheckbox ? sortSurnameCheckbox.checked : false

          const params = new URLSearchParams()
          if (showCashiers) params.append('show_cashiers', showCashiers)
          if (showManagers) params.append('show_managers', showManagers)
          if (sortBySurname) params.append('sort', sortBySurname)

          if (params.size > 0) {
            const response = await fetch(`http://localhost:8090/employees/filter?${params.toString()}`, {
              method: 'GET',
              headers: {
                'Content-Type': 'application/json'
              }
            })

            if (!response.ok) {
              throw new Error(`Failed to filter employees. Status: ${response.status}`)
            }

            this.employees = await response.json()
            this.filtersApplied = true
          } else {
            await this.loadEmployees()
            this.filtersApplied = false
          }
        } catch (error) {
          console.error('Error applying filters to employees:', error)
          alert('Failed to apply filters to employees. Please try again.')
        } finally {
          this.isLoading = false
        }
      },
      clearEmployeeFilters() {
        const showCashiersCheckbox = document.getElementById('show-cashiers')
        const showManagersCheckbox = document.getElementById('show-managers')
        const sortSurnameCheckbox = document.getElementById('sort-surname')

        if (showCashiersCheckbox) showCashiersCheckbox.checked = false
        if (showManagersCheckbox) showManagersCheckbox.checked = false
        if (sortSurnameCheckbox) sortSurnameCheckbox.checked = false

        this.loadEmployees()
        this.filtersApplied = false
      },

    },
    async mounted() {
      const urlParams = new URLSearchParams(window.location.search)
      const categoryName = urlParams.get('category')

      try {
        await this.loadDataForCurrentPage()
        if (categoryName) {
          this.selectedFilter = categoryName
          await this.loadProductsByCategory(categoryName)
        }
      } catch (error) {
        console.error('Error during mounted lifecycle:', error)
      } finally {
        this.isLoading = false
      }
    }
  }
)

app.component("navbar", {
  props: ["userRole"],
  template: `
    <div class="nav-wrapper">
      <nav class="top-nav">
        <div class="nav-top">
          <div class="logo">
            <img src="../images/logo-white.svg" alt="Zlagoda logo">
          </div>
          <h1>Zlagoda</h1>
          <div class="login">
            <button class="login-btn" @click="loginPageDirect">
              {{ loginLabel }}
             </button>
            <span class="material-symbols-outlined">person</span>
          </div>
        </div>
      </nav>
      <nav class="nav-pages-bar">
        <ul>
          <li
            v-for="item in filteredNavItems"
            :key="item.path"
            :class="{ active: isActive(item.path) }"
            :title="'Go to ' + item.label + ' page'"
          >
            <a :href="item.path">{{ item.label }}</a>
          </li>
        </ul>
      </nav>
    </div>
    
    <div class="login-popup">
        <ul class="account-options">
          <li>
            <div class="login-label">
              <span>
                {{ loginLabel }}
               </span>
            <span class="material-symbols-outlined">person</span>
            </div>
          </li>
          <li><a href="">My Profile</a></li>
          <li><a href="">Logout</a></li>
        </ul>
    </div>
    `,
  data() {
    return {
      navItems: [
        { path: 'categories.html', label: 'Categories' },
        { path: 'products.html', label: 'Products' },
        { path: 'customers.html', label: 'Customers' },
        { path: 'checks.html', label: 'Checks' },
        { path: 'employees.html', label: 'Employees' }
      ],
      currentPath: window.location.pathname,
    }
  },
  computed: {
    loginLabel() {
      if (this.userRole === "Unauthorized") return "Log in"
      return this.userRole
    },
    filteredNavItems() {
      switch (this.userRole) {
        case 'Manager':
          return this.navItems
        case 'Cashier':
          return this.navItems.filter(item =>
            ['categories.html', 'products.html', 'checks.html'].includes(item.path))
        default:
          return this.navItems.filter(item =>
            ['categories.html', 'products.html'].includes(item.path))
      }
    }
  },
  methods: {
    isActive(path) {
      const currentPathArray = this.currentPath.split('?')[0].split('/')
      const currentPage = currentPathArray[currentPathArray.length - 1]
      const pathDict = {
        'product-page.html': 'products.html',
        'new-product-page.html': 'products.html',
        'employee-page.html': 'employees.html',
        'new-employee-page.html': 'employees.html',
        'customer-page.html': 'customers.html',
        'new-customer-page.html': 'customers.html',
        'check-page.html': 'checks.html',
        'new-check-page.html': 'checks.html'
      }
      return currentPage === path || (currentPage in pathDict & path === pathDict[currentPage])
    },
    loginPageDirect() {
      // if (this.userRole === "Unauthorized") {
      window.location.href = "index.html"
      // }
    }
  }
})

app.component("custom-footer", {
  template:
    `
    <footer>
      <div class="contacts-bar">
        <p>Copyright &copy 2025 Bratiuk, Tsepkalo, Malii</p>
      </div>  
    </footer>
    `
})

app.component("product-card", {
  template:
    `
    <div class="product-card">
    
      <a :href="'product-page.html?id=' + product.upc">
        <div class="product-image">
          <img :src="product.image" :alt="product.product.product_name">
        </div>
      </a>

      <div class="product-details">
        <div class="product-name">
          <a :href="'product-page.html?id=' + product.upc">{{ product.product.product_name }}</a>
        </div>

        <div class="product-price">
          <div class="product-discount" v-if="product.promotional_product">
            <span class="product-old-price">
              <del class="custom-strike">
                {{ product.selling_price.toFixed(2) }} &#x20B4
              </del>
            </span>
            <span class="product-new-price">
              {{ product.new_price.toFixed(2) }} &#x20B4
            </span>
          </div>
          <span v-else>
            {{ product.selling_price.toFixed(2) }} &#x20B4
          </span>
        </div>
      </div>
    </div>
  `,
  props: ['product'],
  data() {
    return {
      inventory: []
    }
  }
})

app.component("edit-button", {
  props: ["isManager", "isEditMode"],
  emits: ["toggle"],
  template:
    `
    <button
        class="edit-btn"
        v-if="isManager && !isEditMode"
        @click="$emit('toggle')"
      >
        Edit
    </button>
    `
})

app.mount("#app")