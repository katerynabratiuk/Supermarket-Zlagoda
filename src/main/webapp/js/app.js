let app = Vue.createApp(
  {
    data() {
      return {
        isLoading: true,
        isEditMode: false,
        showFilter: false,
        isPasswordVisible: false,

        selectedCategoryNumber: 0,

        token: '',
        isLoggedIn: false,
        user: {
          userRole: 'MANAGER',
          userName: ''
        },
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
          UPC_prom: null,
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

        productsItemsToShowCount: 12,
        productsItemsIncrementBy: 12,
        rowsToShowCount: 10,
        rowsIncrementBy: 10,

        search: '',
        search_result: [],

        productSearchQuery: '',
        categoryFilter: '',
        customerSearchQuery: '',
        discountFilter: null,
        employeeRoleFilter: null,
        sortByNameCust: false,

        error: null
      }
    },
    computed: {
      isManager() {
        return this.user.userRole === "MANAGER"
      },
      isCashier() {
        return this.user.userRole === "CASHIER"
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

      discountAmount() {
        return this.subtotal * (this.discountPercent / 100)
      },

      vatAmount() {
        return (this.subtotal - this.discountAmount) * 0.2
      },

      totalAfterDiscount() {
        return (this.subtotal - this.discountAmount) + this.vatAmount
      },

      filteredProducts() {
        let filtered = this.products
        if (this.productSearchQuery) {
          const q = this.productSearchQuery.toLowerCase()
          filtered = filtered.filter(prod =>
            prod.product.product_name.toLowerCase().includes(q) ||
            (prod.product.description || '').toLowerCase().includes(q) ||
            ((prod.product.category?.category_number?.toString() || '').toLowerCase().includes(q))
          )
        }
        if (this.categoryFilter) {
          const cat = Number(this.categoryFilter)
          if (!isNaN(cat)) {
            filtered = filtered.filter(prod =>
              prod.product.category?.category_number === cat
            )
          }
        }
        if (this.productTypeFilter === 'promotional') {
          filtered = filtered.filter(prod => prod.promotional_product)
        } else if (this.productTypeFilter === 'non-promotional') {
          filtered = filtered.filter(prod => !prod.promotional_product)
        }
        return filtered
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

          if (!response.ok) {
            const status = response.status
            const contentType = response.headers.get('content-type') || ''

            let errorMessage = 'An unknown error occurred'
            if (contentType.includes('application/json')) {
              const errorData = await response.json()
              errorMessage = errorData.message || errorMessage
            } else {
              const errorText = await response.text()
              errorMessage = errorText || errorMessage
            }

            if (status === 401) {
              this.showError('Incorrect username or password')
            } else if (status === 404) {
              this.showError('Username not found')
            } else if (status === 400) {
              this.showError('Invalid login request')
            } else {
              this.showError('An unknown error occurred')
            }
            return
          }
          else {
            const data = await response.json()
            console.log('Login successful', data)
            localStorage.setItem('authToken', data.token)
            this.token = data.token
            this.isLoggedIn = true

            const userResponse = await fetch("http://localhost:8090/auth/me", {
              headers: {
                "Authorization": "Bearer " + localStorage.getItem("authToken")
              }
            })
            const userData = await userResponse.json()
            console.log(userData)

            localStorage.setItem('userData', JSON.stringify(userData))

            this.user = {
              userName: userData.username,
              userRole: userData.role
            }
            window.location.href = 'categories.html'
          }
        } catch (error) {
          this.showError('Login error:')
          this.showError('Unexpected error during login')
          this.showError('Login error:')
        }
      },

      displayedItems(listName) {
        return (array) => {
          if (!array || !Array.isArray(array)) {
            this.showError(`Invalid array passed to displayedItems method: ${listName}`)
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

      showError(message, options = {}) {
        this.error = {
          message,
          timeout: options.timeout || 6000
        }

        if (this.error.timeout) {
          setTimeout(() => {
            this.error = null
          }, this.error.timeout)
        }
      },
      dismissError() {
        this.error = null
      },

      async getProductById(id) {
        try {
          const response = await fetch(`http://localhost:8090/product/${id}`, {
            method: 'GET',
            headers: {
              'Content-Type': 'application/json',
              'Authorization': `Bearer ${this.token}`
            }
          })

          if (!response.ok) {
            this.showError(`Failed to fetch product. Status: ${response.status}`)
          }

          const product = await response.json()
          return product

        } catch (error) {
          this.showError("An error occurred during fetching product:")
          this.showError("An unexpected error occurred. Please try again later.")
          return null
        }
      },
      async getEmployeeById(id) {
        try {
          const response = await fetch(`http://localhost:8090/employee/${id}`, {
            method: 'GET',
            headers: {
              'Content-Type': 'application/json',
              'Authorization': `Bearer ${this.token}`
            }
          })

          if (!response.ok) {
            this.showError(`Failed to fetch employee. Status: ${response.status}`)
          }
          const employee = await response.json()
          return employee

        } catch (error) {
          this.showError("An error occurred during fetching employee:")
          this.showError("An unexpected error occurred. Please try again later.")
          return null
        }
      },
      async getCustomerById(id) {
        try {
          const response = await fetch(`http://localhost:8090/customer/${id}`, {
            method: 'GET',
            headers: {
              'Content-Type': 'application/json',
              'Authorization': `Bearer ${this.token}`
            },
          })

          if (!response.ok) {
            this.showError(`Failed to fetch customer. Status: ${response.status}`)
          }
          const customer = await response.json()
          return customer

        } catch (error) {
          this.showError("An error occurred during fetching customer:")
          this.showError("An unexpected error occurred. Please try again later.")
          return null
        }
      },
      async getCheckById(id) {
        try {
          const response = await fetch(`http://localhost:8090/check/${id}`, {
            method: 'GET',
            headers: {
              'Content-Type': 'application/json',
              'Authorization': `Bearer ${this.token}`
            },
          })

          if (!response.ok) {
            this.showError(`Failed to fetch check. Status: ${response.status}`)
          }
          const check = await response.json()
          return check

        } catch (error) {
          this.showError("An unexpected error occurred. Please try again later.")
          return null
        }
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
                this.currentCustomer = check.customer_card
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
          }
        } catch (error) {
          this.showError('Error loading data for page:', page)
        } finally {
          this.isLoading = false
        }
      },
      async loadCategories() {
        try {
          const response = await fetch("http://localhost:8090/category", {
            headers: {
              'Authorization': `Bearer ${this.token}`
            }
          })

          if (!response.ok) this.showError("Fetch categories error! Status: ${response.status}")

          this.productsCategories = await response.json()
        } catch (error) {
          this.showError("Could not load categories:")
        }
      },
      async loadProducts() {
        try {
          const response = await fetch("http://localhost:8090/product", {
            headers: {
              'Authorization': `Bearer ${this.token}`
            }
          })
          if (!response.ok) this.showError("Fetch products error")

          this.products = await response.json()
        } catch (error) {
          this.showError("Error loading product:")
        }
      },
      async loadProductsByCategory(category_name) {
        this.isLoading = true
        try {
          const response = await fetch(`http://localhost:8090/product/by-category/${category_name}`, {
            headers: {
              'Authorization': `Bearer ${this.token}`,
              'Content-Type': 'application/json'
            }
          })
          if (response.ok) {
            this.products = await response.json()
          } else {
            this.showError("Failed to load products. Status.", response.status)
          }
        } catch (error) {
          this.showError("Error loading products by category.")
        } finally {
          this.isLoading = false
        }
      },
      async loadCustomers() {
        try {
          const response = await fetch("http://localhost:8090/customer", {
            headers: {
              'Authorization': `Bearer ${this.token}`,
              'Content-Type': 'application/json'
            }
          })
          if (!response.ok) this.showError("Fetch customers error! Status: ${response.status}")

          this.customers = await response.json()
        } catch (error) {
          this.showError("Could not load customers:")
        }
      },

      formatCustomerName(customer) {
        const patronymic = customer.cust_patronymic ? ` ${customer.cust_patronymic}` : ''
        return `${customer.cust_surname} ${customer.cust_name}${patronymic}`
      },

      async loadChecks() {
        try {
          const response = await fetch("http://localhost:8090/check", {
            method: 'GET',
            headers: {
              'Authorization': `Bearer ${this.token}`,
              'Content-Type': 'application/json'
            }
          })

          if (!response.ok) this.showError(`Fetch checks error! Status: ${response.status}`)

          this.checks = await response.json()
        } catch (error) {
          this.showError("Could not load checks:")
        }
      },
      async loadEmployees() {
        try {
          const response = await fetch("http://localhost:8090/employee", {
            headers: {
              'Authorization': `Bearer ${this.token}`,
              'Content-Type': 'application/json'
            }
          })

          if (!response.ok) this.showError(`Fetch employees error! Status: ${response.status}`)

          this.employees = await response.json()
        } catch (error) {
          this.showError("Could not load employees:")
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
              headers: {
                'Authorization': `Bearer ${this.token}`,
                'Content-Type': 'application/json'
              }
            })

            if (response.ok) {
              this.productsCategories = this.productsCategories.filter(item => item.id !== categoryIdToDelete)
              this.newCategory = { category_name: '', category_number: null }
              await this.loadCategories()
            } else {
              this.showError("Deletion failed on the server. Status:", response.status)
              this.showError("Failed to delete category. Please try again.")
            }
          } catch (error) {
            this.showError("An error occurred during deletion:")
            this.showError("An unexpected error occurred. Please try again later.")
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
              'Authorization': `Bearer ${this.token}`
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
            this.showError("Adding category failed on the server. Status:", response.status)
            this.showError("Failed to add category. Please try again.")
          }
        } catch (error) {
          this.showError("An unexpected error occurred during adding:")
          this.showError("An unexpected error occurred. Please try again later.")
        }
      },
      async saveEditCategory(category) {
        try {
          const response = await fetch(`http://localhost:8090/category`, {
            method: 'PUT',
            headers: {
              'Content-Type': 'application/json',
              'Authorization': `Bearer ${this.token}`
            },
            body: JSON.stringify(category),
          })

          if (response.ok) {
            this.currentEditingItemID = null
          } else {
            this.showError("Updating category failed on the server. Status:", response.status)
            this.showError("Failed to update category. Please try again.")
          }
        } catch (error) {
          this.showError("An unexpected error occurred during updating:")
          this.showError("An unexpected error occurred. Please try again later.")
        }
      },
      setCategorySort(sortType) {
        this.categorySortBy = sortType
        this.sortCategories()
      },
      async sortCategories() {
        try {
          this.isLoading = true
          const param = this.categorySortBy ? `sortBy=${this.categorySortBy}` : ''
          const response = await fetch(`http://localhost:8090/category/filter?${param}`, {
            headers: {
              'Content-Type': 'application/json',
              'Authorization': `Bearer ${this.token}`
            }
          })
          if (!response.ok) {
            this.showError(`HTTP error! status: ${response.status}`)
          }
          this.productsCategories = await response.json()
        } catch (error) {
          this.showError('Error sorting categories:')
          this.showError('Failed to sort categories. Please try again.')
        } finally {
          this.isLoading = false
        }
      },

      initPriceInput() {
        this.priceInput = this.currentProduct.selling_price?.toFixed(4) || '0.0000'
      },

      handlePriceInput(event) {
        const input = event.target
        let value = input.value

        value = value.replace(/[^\d.]/g, '')
          .replace(/(\..*)\./g, '$1')

        const parts = value.split('.')
        let wholePart = parts[0].replace(/\D/g, '')
        let decimalPart = parts[1] || ''

        if (wholePart.length > 9) wholePart = wholePart.slice(0, 9)
        if (decimalPart.length > 4) decimalPart = decimalPart.slice(0, 4)

        value = wholePart
        if (decimalPart) value += '.' + decimalPart

        this.priceInput = value
        this.currentProduct.selling_price = parseFloat(value) || 0
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
        const productId = this.currentProduct.UPC
        if (confirm("Are you sure you want to delete this product?")) {
          try {
            const response = await fetch(`http://localhost:8090/product/${productId}`, {
              method: 'DELETE',
              headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${this.token}`
              }
            })

            if (response.ok) {
              this.products = this.products.filter(item => item.id !== productId)
              this.currentProduct = null
              window.location.href = 'products.html'
            } else {
              this.showError("Deletion failed on the server. Status:", response.status)
              this.showError("Failed to delete product. Please try again.")
            }
          } catch (error) {
            this.showError("An error occurred during deletion:")
            this.showError("An unexpected error occurred. Please try again later.")
          }
        } else {
          console.log("Deletion cancelled by user.")
        }
      },
      async addNewProduct() {

        const selectedCategory = this.productsCategories.find(
          cat => cat.category_number == this.selectedCategoryNumber
        )

        if (selectedCategory) {
          this.newProduct.product.category = {
            category_number: selectedCategory.category_number,
            category_name: selectedCategory.category_name
          }
        }

        try {
          const response = await fetch('http://localhost:8090/product', {
            method: 'POST',
            headers: {
              'Content-Type': 'application/json',
              'Authorization': `Bearer ${this.token}`
            },
            body: JSON.stringify(this.newProduct),
          })

          if (response.ok) {
            this.newProduct = {
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
              UPC_prom: null,
              products_number: 0,
              new_price: null,
            },
              console.log("New product added successfully:")
            window.location.href = `products.html`
          } else {
            this.showError("Adding product failed on the server. Status:", response.status)
            this.showError("Failed to add product. Please try again.")
          }
        } catch (error) {
          this.showError("An unexpected error occurred during adding:")
          this.showError("An unexpected error occurred. Please try again later.")
        }
      },
      async saveEditProduct() {
        try {
          const isPromotional = this.currentProduct.promotional === true

          const url = isPromotional
            ? 'http://localhost:8090/product/promotional'
            : `http://localhost:8090/product`
          const method = isPromotional ? 'POST' : 'PUT'

          const response = await fetch(url, {
            method,
            headers: {
              'Content-Type': 'application/json',
              'Authorization': `Bearer ${this.token}`
            },
            body: JSON.stringify(this.currentProduct),
          })

          if (response.ok) {
            this.currentEditingItemID = null
            window.location.href = "products.html"
          } else {
            this.showError("Failed to save product. Please try again.")
          }
        } catch (error) {
          this.showError("An unexpected error occurred. Please try again later.")
        }
      },
      async applyProductFilters() {
        try {
          this.isLoading = true

          const categorySelect = document.getElementById('category-select')
          const fromDateInput = document.getElementById('from-date')
          const toDateInput = document.getElementById('to-date')

          let categoryName = null
          const categoryNumber = categorySelect ? categorySelect.value : null
          if (categorySelect && categorySelect.options && categorySelect.selectedIndex !== -1) {
            categoryName = categorySelect.options[categorySelect.selectedIndex].dataset.name
          }

          const fromDate = fromDateInput?.value || null
          const toDate = toDateInput?.value || null
          const productType = this.productTypeFilter

          const params = new URLSearchParams()

          if (categoryNumber) params.append('category', categoryName)
          if (fromDate) params.append('from_date', fromDate)
          if (toDate) params.append('to_date', toDate)
          if (productType === 'promotional') params.append('promotional', true)
          else if (productType === 'non-promotional') params.append('promotional', false)

          if (this.sortProductsParamsField?.length > 0) {
            this.sortProductsParamsField.forEach(field => {
              params.append('sortBy', field)
            })
          }

          const response = await fetch(`http://localhost:8090/product/filter?${params.toString()}`, {
            method: 'GET',
            headers: {
              'Content-Type': 'application/json'
            }
          })

          if (!response.ok) {
            throw new Error(`Failed to filter products. Status: ${response.status}`)
          }

          const data = await response.json()
          this.products = data.products || data
          this.totalPieces = data.total_pieces || 0
          this.filtersApplied = true
          this.currentCategory = { category_name: categoryName }
        } catch (error) {
          console.error('Error applying filters to products:', error)
          this.showError('Failed to apply filters to products. Please try again.')
        } finally {
          this.isLoading = false
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
        this.productTypeFilter = null

        this.loadProducts()
        this.filtersApplied = false
        this.totalPieces = 0
        this.currentProduct = null
        this.isUPCFiltered = false
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
              'Authorization': `Bearer ${this.token}`
            },
            body: JSON.stringify(this.newCustomer),
          })

          if (response.ok) {
            window.location.href = `customers.html`
          } else {
            this.showError("Adding customer failed on the server. Status:", response.status)
            this.showError("Failed to add customer. Please try again.")
          }
        } catch (error) {
          console.error(error)
          this.showError("An unexpected error occurred during adding:")
          this.showError("An unexpected error occurred. Please try again later.")
        }
      },

      async saveEditCustomer() {
        try {
          const response = await fetch(`http://localhost:8090/customer`, {
            method: 'PUT',
            headers: {
              'Content-Type': 'application/json',
              'Authorization': `Bearer ${this.token}`
            },
            body: JSON.stringify(this.currentCustomer)
          })

          if (response.ok) {
            this.currentCustomer = null
            window.location.href = "customers.html"
          } else {
            this.showError("Failed to update customer. Please try again.")
          }
        } catch (error) {
          this.showError("An unexpected error occurred. Please try again later.")
        }
      },
      async confirmAndDeleteCustomer() {
        const customerId = this.currentCustomer.card_number
        if (confirm("Are you sure you want to delete this customer?")) {
          try {
            const response = await fetch(`http://localhost:8090/customer/${customerId}`, {
              method: 'DELETE',
              headers: {
                'Content-type': 'application/json',
                'Authorization': `Bearer ${this.token}`
              }
            })

            if (response.ok) {
              window.location.href = 'customers.html'
            } else {
              this.showError("Deletion failed on the server. Status:", response.status)
              this.showError("Failed to delete customer. Please try again.")
            }
          } catch (error) {
            this.showError("An unexpected error occurred during deletion:")
            this.showError("An unexpected error occurred. Please try again later.")
          }
        } else {
          console.log("Deletion cancelled by user.")
        }
      },

      async applyCustomerFilters() {
        const params = {}
        if (this.discountFilter !== null) {
          params.percentage = this.discountFilter
        }
        if (this.sortByNameCust) {
          params.sortBy = 'name'
        }

        try {
          const response = await axios.get('http://localhost:8090/customer/filter', { params })
          this.customers = response.data
        } catch (error) {
          this.showError('Filtering customers failed.')
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

      async searchCustomers() {
        if (!this.customerSearchQuery || this.customerSearchQuery.trim() === '') {
          await this.loadCustomers()
          return
        }
        try {
          const response = await axios.get('http://localhost:8090/customer/search', {
            params: { search: this.customerSearchQuery },
            headers: {
              'Authorization': `Bearer ${this.token}`
            }
          })
          this.customers = response.data
        } catch (error) {
          this.showError('An error occurred while searching for customers.')
        }
      },

      async searchProducts() {
        if (!this.productSearchQuery) {
          await this.loadProducts()
          return
        }

        try {
          const response = await fetch(
            `http://localhost:8090/product/search?query=${encodeURIComponent(this.productSearchQuery)}`, {
            headers: {
              'Authorization': `Bearer ${this.token}`
            }
          }
          )
          if (!response.ok) throw new Error("Search failed")

          this.products = await response.json()
        } catch (error) {
          console.error("Search error:", error)
          this.showError("An error occurred during product search.")
        }
      },

      goToAddCheck() {
        window.location.href = 'new-check-page.html'
      },
      async addNewCheck() {
        this.newCheck.sum_total = this.totalAfterDiscount
        this.newCheck.vat = this.vatAmount
        try {
          const response = await fetch('http://localhost:8090/check', {
            method: 'POST',
            headers: {
              'Content-Type': 'application/json',
              'Authorization': `Bearer ${this.token}`
            },
            body: JSON.stringify(this.newCheck),
          })

          if (response.ok) {
            console.log("New check added successfully:")
            window.location.href = 'checks.html'
          }
        } catch (error) {
          this.showError("An unexpected error occurred. Please try again later.")
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
          this.showError("Error fetching product:")
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
              this.showError("Error fetching customer:")
              this.currentCustomer = null
            })
        } else {
          this.currentCustomer = null
        }
      },

      async applyCheckFilters() {
        try {
          this.isLoading = true

          const cashierSelect = document.getElementById('cashier-select')
          const fromDateInput = document.getElementById('from-date')
          const toDateInput = document.getElementById('to-date')
          const showTotalSumCheckbox = document.getElementById('show-total-sum')
          const sortByInput = document.querySelector('input[name="sortby"]:checked')
          const sortBy = sortByInput ? sortByInput.value : null

          const cashierId = cashierSelect?.value || null
          const fromDate = fromDateInput?.value || null
          const toDate = toDateInput?.value || null
          const showTotalSum = showTotalSumCheckbox?.checked || false

          this.showTotalSumChecked = showTotalSum
          this.totalSum = 0

          const params = new URLSearchParams()
          if (cashierId) params.append('cashierId', cashierId)
          if (fromDate) params.append('from', fromDate)
          if (toDate) params.append('to', toDate)
          if (sortBy) params.append('sortBy', sortBy)

          if (showTotalSum && fromDate && toDate) {
            const baseUrl = 'http://localhost:8090/api/sales/total'
            const sumUrl = cashierId
              ? `${baseUrl}/cashier/${cashierId}?start=${fromDate}&end=${toDate}`
              : `${baseUrl}/all?start=${fromDate}&end=${toDate}`

            const totalSumResponse = await fetch(sumUrl, {
              method: 'GET',
              headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${this.token}`
              }
            })

            if (!totalSumResponse.ok) {
              this.showError(`Failed to load total sum. Status: ${totalSumResponse.status}`)
            } else {
              const totalSumData = await totalSumResponse.json()
              this.totalSum = totalSumData.total || 0
            }
          }

          if (params.size > 0) {
            const response = await fetch(`http://localhost:8090/check/filter?${params.toString()}`, {
              method: 'GET',
              headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${this.token}`
              }
            })

            if (!response.ok) {
              this.showError(`Failed to filter checks. Status: ${response.status}`)
            } else {
              const data = await response.json()
              this.checks = data.checks || data
              this.filtersApplied = true
            }
          } else {
            await this.loadChecks()
            this.filtersApplied = false
          }
        } catch (error) {
          this.showError('Failed to apply filters to checks. Please try again.')
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

      formatPhoneNumber(event, type) {
        let value = event.target.value

        if (!value.startsWith('+380')) {
          value = '+380' + value.replace(/[^\d]/g, '').substring(3)
        }

        value = '+' + value.substring(1).replace(/[^\d]/g, '')

        if (value.length > 13) {
          value = value.substring(0, 13)
        }

        if (type === 'employee') {
          this.currentEmployee.phone_number = value
        } else {
          this.currentCustomer.phone_number = value
        }

        event.target.value = value
      },

      initializePhoneNumbers() {
        if (!this.newEmployee.phone_number?.startsWith('+380')) {
          this.newEmployee.phone_number = '+380'
        }

        if (this.currentEmployee && !this.currentEmployee.phone_number?.startsWith('+380')) {
          this.currentEmployee.phone_number = '+380' +
            (this.currentEmployee.phone_number?.replace(/[^\d]/g, '') || '').substring(0, 9)
        }

        if (this.currentCustomer && !this.currentCustomer.phone_number?.startsWith('+380')) {
          this.currentCustomer.phone_number = '+380' +
            (this.currentCustomer.phone_number?.replace(/[^\d]/g, '') || '').substring(0, 9)
        }
      },

      formatPhoneNumber(event, type) {
        const input = event.target
        let value = input.value

        if (!value.startsWith('+380')) {
          value = '+380' + value.replace(/[^\d]/g, '').substring(3)
        }

        value = '+' + value.substring(1).replace(/[^\d]/g, '')

        if (value.length > 13) {
          value = value.substring(0, 13)
        }

        if (type === 'employee') {
          if (this.currentEmployee) {
            this.currentEmployee.phone_number = value
          } else {
            this.newEmployee.phone_number = value
          }
        } else {
          if (this.currentCustomer) {
            this.currentCustomer.phone_number = value
          } else {
            this.newCustomer.phone_number = value
          }
        }

        input.value = value
      },

      preventPrefixDeletion(event) {
        const input = event.target
        if (
          (event.key === 'Backspace' || event.key === 'Delete') &&
          input.selectionStart < 4 &&
          input.value.startsWith('+380')
        ) {
          event.preventDefault()
        }
      },

      goToAddEmployee() {
        window.location.href = 'new-employee-page.html'
      },
      async addNewEmployee() {
        try {
          const response = await fetch('http://localhost:8090/employee', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json', 'Authorization': `Bearer ${this.token}` },
            body: JSON.stringify(this.newEmployee),
          })

          if (response.ok) {
            window.location.href = `employees.html`
          } else {
            this.showError("Failed to add employee. Please try again.")
          }
        } catch (error) {
          this.showError("An unexpected error occurred. Please try again later.")
        }
      },
      async confirmAndDeleteEmployee() {
        const employeeId = this.currentEmployee.id_employee
        if (confirm("Are you sure you want to delete this employee?")) {
          try {
            const response = await fetch(`http://localhost:8090/employee/${employeeId}`, {
              method: 'DELETE',
              headers: {
                'Content-type': 'application\json',
                'Authorization': `Bearer ${this.token}`
              }
            })

            if (response.ok) {
              this.employees = this.employees.filter(item => item.id_employee !== employeeId)
              this.currentEmployee = null
              window.location.href = 'employees.html'
            } else {
              this.showError("Deletion failed on the server. Status:", response.status)
            }
          } catch (error) {
            this.showError("An unexpected error occurred. Please try again later.")
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
              'Authorization': `Bearer ${this.token}`
            },
            body: JSON.stringify(this.currentEmployee),
          })

          if (response.ok) {
            this.currentEmployee = null
            window.location.href = "employees.html"
          } else {
            this.showError("Failed to update employee. Please try again.")
          }
        } catch (error) {
          this.showError("An unexpected error occurred. Please try again later.")
        }
      },
      formatEmployeeName(employee) {
        return `${employee.empl_surname} ${employee.empl_name} ${employee.empl_patronymic || ''}`
      },
      async applyEmployeeFilters() {
        try {
          const params = new URLSearchParams()

          if (this.employeeRoleFilter === 'cashier') {
            params.append('cashier', true)
          } else if (this.employeeRoleFilter === 'manager') {
            params.append('manager', true)
          }

          const sortCheckbox = document.getElementById("sort-surname")
          if (sortCheckbox?.checked) {
            params.append('sortBy', 'name')
          }

          const response = await fetch(`http://localhost:8090/employee/filter?${params.toString()}`, {
            method: 'GET',
            headers: {
              'Content-Type': 'application/json',
              'Authorization': `Bearer ${this.token}`
            }
          })

          if (!response.ok) {
            throw new Error(`Filtering failed. Status: ${response.status}`)
          }

          const data = await response.json()
          this.employees = data
          this.showFilter = false
        } catch (error) {
          console.error("Filter failed", error)
          this.showError("Employee filtering failed.")
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
      async searchEmployees() {
        if (!this.search) {
          this.loadEmployees()
          return
        }
        try {
          const response = await fetch(`http://localhost:8090/employee/search?query=${encodeURIComponent(this.search)}`, {
            method: 'GET',
            headers: {
              'Authorization': `Bearer ${this.token}`,
              'Content-Type': 'application/json'
            }
          })

          if (!response.ok) {
            throw new Error(`Search failed. Status: ${response.status}`)
          }

          const data = await response.json()
          this.employees = data
        } catch (error) {
          console.error("Search failed", error)
          this.showError("Employee search failed")
        }
      },

    },
    async mounted() {
      const urlParams = new URLSearchParams(window.location.search)
      const categoryName = urlParams.get('category')
      const storedToken = localStorage.getItem('authToken')
      const storedUser = localStorage.getItem('userData')

      if (storedToken) {
        this.token = storedToken
        this.isLoggedIn = true
      }

      if (storedUser) {
        try {
          const userData = JSON.parse(storedUser)
          this.user.userName = userData.username
          this.user.userRole = userData.role
        } catch (e) {
          this.showError('Failed to parse user data', e)
        }
      }

      this.initializePhoneNumbers()

      try {
        await this.loadDataForCurrentPage()
        if (categoryName) {
          this.selectedFilter = categoryName
          await this.loadProductsByCategory(categoryName)
        }
      } catch (error) {
        this.showError('Error during mounted lifecycle')
      } finally {
        this.isLoading = false
      }

      if (this.currentProduct && this.currentProduct.promotional_product && this.currentProduct.UPC_prom) {
        const baseProduct = await this.getProductById(this.currentProduct.UPC_prom)
        if (baseProduct) {
          this.currentProduct.prom_base_price = baseProduct.selling_price
        }
      }
    }
  }
)

app.component("navbar", {
  props: ["user", "isLoggedIn"],
  template: `
    <div class="nav-wrapper">
      <nav class="top-nav">
        <div class="nav-top">
          <div class="logo">
            <img src="../images/logo-white.svg" alt="Zlagoda logo">
          </div>
          <h1>Zlagoda</h1>
          <div class="login" ref="loginArea">
            <button class="login-btn" :disable="toggleLoginPopup" @click="login" @mouseover="toggleLoginPopup">
              <span> {{ loginLabel }} </span>
              <span class="material-symbols-outlined">person</span>
            </button>
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
    
    <div class="login-popup" v-if="showLoginPopup && isLoggedIn" @mouseleave="toggleLoginPopup">
        <ul class="account-options">
          <li>
            <div class="login-label">
              <span>
                Welcome, {{ user.userName }}!
               </span>
            </div>
          </li>
          <li><a href="">My Profile</a></li>
          <li><a href="index.html" @click="logout" >Logout</a></li>
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
      showLoginPopup: false,
    }
  },
  computed: {
    loginLabel() {
      if (!this.isLoggedIn) return "Log in"
      return this.user.userName
    },
    filteredNavItems() {
      switch (this.user.userRole) {
        case 'MANAGER':
          return this.navItems
        case 'CASHIER':
          return this.navItems.filter(item =>
            ['categories.html', 'products.html', 'checks.html', 'customers.html'].includes(item.path))
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
    toggleLoginPopup() {
      this.showLoginPopup = !this.showLoginPopup
    },
    login() {
      window.location.href = "index.html"
    },
    logout() {
      localStorage.removeItem('authToken')
      localStorage.removeItem('userData')
      this.token = ''
      this.user = {
        userName: '',
        userRole: 'Unnauthorized'
      }
      this.isLoggedIn = false
      window.location.href = 'index.html'
    },
    async goToMyProfile() {
      try {
        const response = await fetch('http://localhost:8090/auth/me', {
          method: 'GET',
          headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${this.token}`,
          }
        })

        if (!response.ok) {
          throw new Error(`HTTP error! status: ${response.status}`)
        }

        const userData = await response.json()

        if (userData.employeeId) {
          window.location.href = `employee-page.html?id=${userData.employeeId}`
        }
      } catch (error) {
        this.showError("Failed to load profile. Please try again.")
      }
    }
  },
  mounted() {
    document.addEventListener('click', this.handleClickOutside)
  }
})

app.component("custom-footer", {
  template:
    `
    <footer>
      <div class="contacts-bar">
        <p>Copyright &copy 2025 Bratiuk, Malii, Tsepkalo</p>
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

app.component('custom-error', {
  props: {
    message: String,
    duration: {
      type: Number,
      default: 5000
    }
  },
  data() {
    return {
      show: false
    }
  },
  methods: {
    toggleErrorShow() {
      this.show = !this.show
    }
  },
  mounted() {
    this.fetchAllEmployees()
    this.show = true
    if (this.duration > 0) {
      setTimeout(() => {
        this.show = false
      }, this.duration)
    }
  },
  template: `
    <transition name="error-fade">
      <div v-if="show" class="custom-error">
        <div class="message-content">{{ message }}</div>
        <button class="error-btn" @click="toggleErrorShow"> OK </button>
      </div>
    </transition>
  `
})

app.mount("#app")