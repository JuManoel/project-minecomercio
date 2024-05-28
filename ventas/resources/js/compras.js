export default class compras {
    static #salesTable // referencia a la instancia de Tabulator
    static #modal // referencia a una ventana emergente de confirmación
    static #products // el array con la información de productos
    static #provedor // el array de provedors
    static #seller // el array de vendedores
  
    constructor() {
      throw new Error('No utilice el constructor. Use compras.init()')
    }
  
    /**
     * Carga los datos de los provedors y productos para disponer las entradas de datos de la venta y sus líneas de venta
     */
    static async init() {
      Toast.show({ title: 'Registro de compras', message: 'Digite los nombres de los productos en las celdas de la columna "Producto"', duration: 5000 })
      try {
        // asignar a compras.#provedor la data de provedors obtenida mediante fetchData()
        let response = await Helpers.fetchData(`${urlAPI}/provedor`)
        compras.#provedor = response.data
  
        // asignar a compras.#seller la data de vendedores obtenida mediante fetchData()
        response = await Helpers.fetchData(`${urlAPI}/vendedor`)
        compras.#seller = []
        let i=0
        response.data.forEach(element => {
            if(element.admin){
                compras.#seller[i]=element
                i++
            }
          });
        
  
        // inyectar a <main> ./resources/html/compras.html
        document.querySelector('main').innerHTML = await Helpers.loadPage('./resources/html/compras.html')
  
        // asignar a la fecha del formulario la fecha y hora actual
        document.querySelector('#form-compras #fecha').value = DateTime.now().toISO({ includeOffset: false })
  
        // asignar al seleccionable de provedors del formulario de datos, a partir de compras.#provedor
        document.querySelector(`#form-compras #provedor`).innerHTML = Helpers.toOptionList({
          items: compras.#provedor,
          value: 'id',
          text: 'nombre',
          firstOption: 'Seleccione un provedor',
        })
  
        // asignar al seleccionable de vendedores del formulario de datos, a partir de compras.#seller
        document.querySelector(`#form-compras #vendedor`).innerHTML = Helpers.toOptionList({
          items: compras.#seller,
          value: 'id',
          text: 'nombre',
          firstOption: 'Seleccione un vendedor',
        })
        //asignar a compras.#salesTable la instancia de Tabulator
        compras.#salesTable = new Tabulator('main #compras > #table-container', {
          height: 'calc(100vh - 400px)', // establecer la altura de la tabla, esto habilita el DOM virtual y mejora drásticamente la velocidad de procesamiento
          data: [{ cantidad: null, producto: null, valorUnitario: null, iva: null, valorTotal: null }],
          layout: 'fitColumns', // ajustar columnas al ancho de la tabla (opcional)
          columns: [
            { title: 'Cantidad', field: 'cantidad', width: 95, hozAlign: 'center', editor: 'number', cellEdited: compras.#updateRow, editorParams: { min: 1, max: 1000 }, validator: ['required', 'min:1', 'max:1000'] },
            { title: 'Producto', field: 'producto', editor: 'list', validator: 'required', editorParams: { values: await compras.#getProducts(), autocomplete: true }, cellEdited: compras.#updateRow },
            { title: 'Vr. unitario', field: 'valorUnitario', hozAlign: 'right', width: 110, formatter: 'money' },
            { title: 'IVA', field: 'iva', width: 90, bottomCalc: 'sum', bottomCalcFormatter: 'money', hozAlign: 'right', formatter: 'money' },
            { title: 'Vr. total', field: 'valorTotal', width: 110, bottomCalc: 'sum', bottomCalcFormatter: 'money', hozAlign: 'right', formatter: 'money' },
            { formatter: deleteRowButton, width: 40, hozAlign: 'center', cellClick: (e, cell) => cell.getRow().delete() },
          ],
  
          // agregar a la tabla los botones para registrar venta, crear nueva línea de venta y crear venta
          footerElement: `
                      <div class='container-fluid d-flex justify-content-end p-0'>
                          <button id='save-sale' class='btn btn-success btn-sm me-2'>${icons.mineCartLoaded}&nbsp;&nbsp;Registrar Compras</button>
                          <button id='add-row' class='btn btn-info btn-sm me-2'>${icons.plusSquare}&nbsp;&nbsp;Nueva línea</button>
                          <button id='new-sale' class='btn btn-warning btn-sm'>${icons.mineCart}&nbsp;&nbsp;Nueva Compra</button>
                      </div>
                  `.trim(),
        })
        compras.#salesTable.on('tableBuilt', () => {
          // asignar los listener de los botones del footer de la tabla
          document.querySelector('#save-sale').addEventListener('click', compras.#saveSale)
          document.querySelector('#add-row').addEventListener('click', compras.#addRow)
          document.querySelector('#new-sale').addEventListener('click', compras.#newSale)
        })
      } catch (e) {
        Toast.show({ message: 'Sin acceso a la opción de compras', mode: 'danger', error: e })
      }
  
      return this
    }
  
    static async #getProducts() {
      // asignar a compras.#products la data de provedors obtenida mediante fetchData(), excluir los vencidos y los no disponibles
      const response = await Helpers.fetchData(`${urlAPI}/producto`)
  
      // el siguiente filtro reduce la comparación a días para evitar imprecisiones
      compras.#products = response.data.filter(p => DateTime.fromISO(p.vencimiento).endOf('day') >= DateTime.now().endOf('day') && p.disponible > 0)
  
      // en la variable local listProducts mapear compras.#products.map para obtener un array con los nombres de los productos
      return compras.#products.map(({ descripcion }) => descripcion)
    }
  
    /**
     * Cuando se ingresa una cantidad o se elige un producto, se actualiza el precio unitario, el iva y el subtotal de la fila
     * @param {Cell} cell
     */
    static #updateRow(cell) {
      // obtener las líneas de venta
      const salesLines = compras.#salesTable.getData()
      // obtener en la variable local rowData, los datos de la línea de venta a partir del método getData() de la fila de la celda
      const rowData = cell.getRow().getData()
  
      // asignar a la variable local product el producto de compras.#products que coincida con el descripcion de la fila actual
      const product = compras.#products.find(p => p.descripcion === rowData.producto)
  
      if (product) {
        // no permitir el ingreso de líneas de venta con productos duplicados
        const uniqueProducts = new Set(salesLines.map(line => line.producto))
        if (uniqueProducts.size < salesLines.length) {
          Toast.show({ message: 'No se permiten líneas de venta con productos duplicados', mode: 'danger' })
          cell.getRow().delete()
        }
  
        if (rowData.cantidad > product.disponibles) {
          rowData.cantidad = product.disponibles
          Toast.show({ message: 'La cantidad solicitada excede la disponible. Se asignó el máximo disponible', mode: 'danger' })
        }
  
        // asignar en rowData.valorUnitario el valor de venta de product
        rowData.valorUnitario = product.valorBase
        // calcular el IVA de la línea de venta
        rowData.iva = product.valorBase * (product.iva / 100) * rowData.cantidad
        // asignar en rowData.valorTotal el subtotal de la línea actual
        rowData.valorTotal = rowData.cantidad * rowData.valorUnitario + rowData.iva
        // utilizar el método update(rowData) de la fila actual para actualizarla
        cell.getRow().update(rowData)
      }
    }
  
    /**
     * Dispone el formulario y la tabla de líneas de venta para ingresar una nueva venta
     */
    static #newSale() {
      // utilizar table.getData() para asignar a la variable salesLines la data de la tabla
      const salesLines = compras.#salesTable.getData()
      // asigna la la variable someDataInRow true si al menos una fila tiene asignada la cantidad o el producto
      const someDataInRow = salesLines.some(row => row.cantidad > 0 || row.producto)
  
      if (someDataInRow) {
        // si someDataInRow pide confirmación para crear la nueva venta
        ;(compras.#modal = new Modal({
          size: 'default',
          title: '<span class="h5">Nueva venta</span>',
          content: `Hay una venta en curso. ¿Continuar de todos modos?`,
          buttons: [
            {
              caption: `${icons.mineCart}&nbsp;&nbsp;<span>Nueva venta</span>`,
              classes: 'btn btn-warning btn-sm me-2',
              action: () => {
                // limpiar el formulario - clearForm()
                compras.clearForm()
                compras.#modal.close()
              },
            },
            {
              caption: cancelButton,
              classes: 'btn btn-secondary btn-sm',
              action: () => compras.#modal.close(),
            },
          ],
        })).show()
      } else {
        // limpiar el formulario - clearForm()
        compras.clearForm()
      }
    }
  
    /**
     * Limpia las entradas del formulario
     */
    static clearForm() {
      // refresca la fecha y la hora actual en el formulario
      document.querySelector('#form-compras #fecha').value = DateTime.now().toISO({ includeOffset: false })
      // establecer en la data de la tabla sólo una línea en blanco
      compras.#salesTable.setData([{ cantidad: null, producto: null, valorUnitario: null, valorTotal: null }])
      // La lista seleccionable de provedors muestra vuelve al elemento 0: "Seleccione un provedor"
      compras.selectableProvedorAtIndex0
    }
  
    /**
     * Verifica que los datos del formulario y de la tabla sean correctos y envía una solicitud POST con dichos datos
     */
    static async #saveSale() {
      const fechaHora = document.querySelector(`#form-compras #fecha`).value
      let json = {
        provedor: null,
        vendedor: null,
        detalles: null,
        fechaHora: fechaHora,
      }
      const idc = document.querySelector(`#form-compras #provedor`).value
      const idv = document.querySelector(`#form-compras #vendedor`).value
      if (idc == '') {
        Toast.show({ message: 'Ingrese el Provedor' })
        return
      }
      json.provedor = idc
      if (idv == '') {
        Toast.show({ message: 'Ingrese el Vendedor' })
        return
      }
      json.vendedor = idv
      json.detalles = []
      let i = 0
      const response = await Helpers.fetchData(`${urlAPI}/producto`)
      let productos = response.data
      let id
      compras.#salesTable.getData().forEach(detalle => {
        id = null
        if (detalle.cantidad == '' || detalle.cantidad === null) {
          Toast.show({ message: 'Ingrese la Cantidad' })
          return
        }
        if (detalle.producto == '' || detalle.producto == null) {
          Toast.show({ message: 'Ingrese el Producto' })
          return
        }
        productos.forEach(element => {
          if (element.descripcion == detalle.producto) {
            id = element.id
          }
        })
        if (id === null) {
          Toast.show({ message: 'Ingrese un Producto que exista' })
          return
        }
        json.detalles[i] = {
          cantidad: detalle.cantidad,
          producto: id,
        }
        i++
      })
      console.log(json)
      const body = json
      try {
        let response = await Helpers.fetchData(`${urlAPI}/compra`, {
          method: 'POST',
          body,
        })
        if (response.message === 'ok') {
          compras.#salesTable.getColumnDefinitions()[1].editorParams.values = await compras.#getProducts()
          Toast.show({ message: 'Venta registrada exitosamente' })
          compras.clearForm()
        } else {
          Toast.show({ message: 'No se pudo agregar la venta', mode: 'danger', error: response })
        }
      } catch (e) {
        Toast.show({ message: 'Sin acceso al registro de compras', mode: 'danger', error: e })
      }
      return
    }
  
    /**
     * Forza un evento change sobre el seleccionable de provedors para volver al elemento "Seleccione un provedor"
     */
    static selectableProvedorAtIndex0() {
      // en la variable local selectList referencia al seleccionable de provedors
      const selectList = document.querySelector('#form-compras #provedor')
      // posiciona al seleccionable en el índice 0
      selectList.selectedIndex = 0
      // forza el evento change
      selectList.dispatchEvent(new Event('change'))
    }
  
    /**
     * agrega un fila a la tabla de líneas de venta
     */
    static #addRow() {
      // si la tabla no tiene filas o todas las filas tienen datos, adicionar una nueva línea de venta con datos nulos
      if (compras.#salesTable.getDataCount() === 0 || compras.#allCellsFilled()) {
        compras.#salesTable.addRow({ cantidad: null, producto: null, valorUnitario: null, valorTotal: null })
      } else {
        // si la condición no se cumple avisar al usuario del por qué no se crea la nueva línea de venta
        Toast.show({ message: 'No se creó la nueva línea de venta. Hay al menos una línea de venta con cantidades no válidas o está incompleta.', mode: 'danger' })
      }
    }
  
    /**
     *
     * @returns
     */
    static #allCellsFilled() {
      // obtener en la variable local  salesLines las líneas de venta, mediante getData()
      const salesLines = compras.#salesTable.getData()
      // verificar si todas las filas tienen la cantidad y el producto
      return salesLines.some(row => row.cantidad > 0 && row.producto)
    }
  
    static async informeCompras() {
      const response = await Helpers.fetchData(`${urlAPI}/compra`)
      let compras = response.data
      await this.#table(compras)
    }
  
    static async #table(nestedData) {
      //define table
      document.querySelector('main').innerHTML = await Helpers.loadPage('./resources/html/compras.html')
      document.querySelector('#form-compras').innerHTML = ''
      console.log(nestedData)
      var table = new Tabulator('main #compras > #table-container', {
        height: tableHeight,
        layout: 'fitColumns',
        columnDefaults: {
          resizable: true,
        },
        data: nestedData,
        columns: [
          { title: 'Nor.', field: 'id' },
          { title: 'Fecha', field: 'fechaHora' },
          { title: 'Provedor', field: 'provedor.nombre' },
          { title: 'Vendedor', field: 'vendedor.nombre' },
          { title: 'Total', field: 'total' },
          { formatter: deleteRowButton, width: 40, hozAlign: 'center', cellClick: (e, cell) => cell.getRow().delete() },
        ],
        rowFormatter: function (row) {
          //create and style holder elements
          console.log(row.getData())
          var holderEl = document.createElement('div')
          var tableEl = document.createElement('div')
  
          holderEl.style.boxSizing = 'border-box'
          holderEl.style.padding = '10px 30px 10px 10px'
          holderEl.style.borderTop = '1px solid #333'
          holderEl.style.borderBotom = '1px solid #333'
  
          tableEl.style.border = '1px solid #333'
  
          holderEl.appendChild(tableEl)
  
          row.getElement().appendChild(holderEl)
            console.log(row.getData().detalles)
          var subTable = new Tabulator(tableEl, {
            layout: 'fitColumns',
            data: row.getData().detalles,
            columns: [
              { title: 'Cant.', field: 'cantidad' },
              { title: 'Nombre', field: 'producto.descripcion' },
              { title: 'Valor', field: 'producto.valorBase' },
              { title: 'IVA', field: 'producto.iva' },
              { title: 'SubTotal', field: 'subTotal' },
            ],
          })
        },
      })
    }
  }
  