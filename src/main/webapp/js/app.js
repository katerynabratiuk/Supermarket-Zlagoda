let app = Vue.createApp(
  {
    data() {
      return {
        isLoading: true,
        isEditMode: false,
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

        newCategory: {
          category_name: '',
          category_number: null
        },

        newProduct: {
          id: null,
          name: '',
          price: null,
          image: '',
          category: '',
          description: '',
          manufacturer: '',
          status: '',
          products_number: 0,
          isOnSale: false,
          new_price: null,
          count: 0
        },
        imagePreview: null,
        currentProduct: null,

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
          zip_code: ''
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
        return {
          'in-stock': this.currentProduct?.products_number !== 0,
          'out-of-stock': this.currentProduct?.products_number === 0
        };
      },
      newStatusClass() {
        return {
          'in-stock': this.newProduct.products_number !== 0,
          'out-of-stock': this.newProduct.products_number === 0
        };
      }
    },
    watch: {
      currentProduct(newVal) {
        if (newVal) {
          document.title = `${newVal.name} - Zlagoda`
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

        if (path.includes('categories.html')) {
          await this.loadCategories()

        } else if (path.includes('products.html')) {
          await this.loadProducts()

        } else if (path.includes('product-page.html')) {
          await Promise.all([this.loadProducts(), this.loadCategories()])
          const productId = new URLSearchParams(window.location.search).get('id')
          console.log(productId)
          if (productId) {
            try {
              const product = await this.getProductById(productId)

              this.currentProduct = product
              console.log(this.currentProduct)
            }
            catch (error) {
              console.error(error)
            }
          }

        } else if (path.includes('new-product-page.html')) {
          await this.loadCategories()

        } else if (path.includes('customers.html')) {
          await this.loadCustomers()

        } else if (path.includes('customer-page.html')) {
          await this.loadCustomers()
          const customerId = new URLSearchParams(window.location.search).get('id')
          if (customerId) {
            try {
              const customer = await this.getCustomerById(customerId)
              this.currentCustomer = customer
            }
            catch (error) {
              console.error(error)
            }
          }
        } else if (path.includes('checks.html')) {
          await this.loadChecks()

        } else if (path.includes('check-page.html')) {
          await Promise.all([
            this.loadChecks(),
            this.loadProducts(),
            this.loadEmployees(),
            this.loadCustomers(),
          ])
          const checkId = new URLSearchParams(window.location.search).get('id')
          try {
            const check = await this.getCheckById(checkId)
            this.currentCheck = check

            const employee = await this.getEmployeeById(this.currentCheck.id_employee)
            const customer = await this.getCustomerById(this.currentCheck.card_number)

            this.currentCheck.employeeName = `${employee.empl_surname} ${employee.empl_name} ${employee.empl_patronymic || ''}`
            this.currentCheck.customerName = `${customer.cust_surname} ${customer.cust_name} ${customer.cust_patronymic || ''}`
          } catch (error) {
            console.error(error)
          }
        } else if (path.includes('new-check-page.html')) {
          await Promise.all([
            this.loadProducts(),
            this.loadEmployees(),
            this.loadCustomers(),
          ])
        } else if (path.includes('employees.html')) {
          await this.loadEmployees()

        } else if (path.includes('employee-page.html')) {
          await this.loadEmployees()
          const employeeId = new URLSearchParams(window.location.search).get('id')
          if (employeeId) {
            try {
              const employee = await this.getEmployeeById(employeeId)
              this.currentEmployee = employee
            }
            catch (error) {
              console.error(error)
            }
          }
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
            const response = await fetch(`/api/products/${productId}`, {
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
          const response = await fetch('/api/products/add', {
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
              isOnSale: false,
              new_price: null,
              count: 0
            }
            console.log("New product added successfully:", newProduct)
            window.location.href = `product-page.html?id=${newProduct.id}`
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
          const response = await fetch(`/api/products/${this.currentProduct.id}`, {
            method: 'PATCH',
            headers: {
              'Content-Type': 'application/json',
            },
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
            window.location.href = `customer-page.html?id=${newCustomer.card_number}`

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
          const response = await fetch(`http://localhost:8090/customer/${this.currentCustomer.card_number}`, {
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
      async confirmAndDeleteCheck() {
        const checkNumber = this.currentCheck.check_number
        if (confirm("Are you sure you want to delete this check?")) {
          try {
            const response = await fetch(`...`, {
              method: 'DELETE',
            })

            if (response.ok) {
              this.check = this.check.filter(item => item.check_number !== checkNumber)
              this.currentCheck = null
              window.location.href = 'checks.html'
            } else {
              console.error("Deletion failed on the server. Status:", response.status)
              alert("Failed to delete check. Please try again.")
            }
          } catch (error) {
            console.error("An unexpected error occurred during deletion:", error)
            alert("An unexpected error occurred. Please try again later.")
          }
        } else {
          console.log("Deletion cancelled by user.")
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

        if (productId) {
          try {
            const selectedProduct = await this.getProductById(productId)

            if (selectedProduct) {
              this.newCheck.sales[index].selling_price = selectedProduct.price
              this.newCheck.sales[index].product_name = selectedProduct.name
            } else {
              this.newCheck.sales[index].selling_price = 0
              this.newCheck.sales[index].product_name = ''
            }
          } catch (error) {
            console.error("Error fetching product:", error)
          }
        } else {
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
      goToAddEmployee() {
        window.location.href = 'new-employee-page.html'
      },
      async addNewEmployee() {
        try {
          const response = await fetch('http://localhost:8090/employee', {
            method: 'POST',
            headers: {
              'Content-Type': 'application/json',
            },
            body: JSON.stringify(this.newEmployee),
          })

          if (response.ok) {
            const newEmployee = await response.json()
            this.employees.push(newEmployee)
            console.log("New employee added successfully:", newEmployee)
            window.location.href = `employee-page.html?id=${newEmployee.id_employee}`
          } else {
            console.error("Adding employee failed on the server. Status:", response.status)
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
    },
    mounted() {
      this.loadDataForCurrentPage()
      this.isLoading = false
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