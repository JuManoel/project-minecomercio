/**
 * Basado en
 * https://codepen.io/ng-ngc-sn-the-bashful/pen/Exgmxqp
 */
export default class Toast {
  /**
   * Ejemplo de uso:
   * Toast.show({
   *   title: 'Sistema de ventas',
   *   message: 'Producto creado correctamente',
   *   mode: 'success', // también: 'info' | 'warning' | 'danger'
   *   duration: 5000,
   * })
   */

  /**
   *
   * @param {*} Un objeto
   */
  static show({ title = '', message = '', mode = 'info', duration = 3000, error = null }) {
    document.querySelector('body').insertAdjacentHTML('afterbegin', '<div id="toast"></div>')
    const container = document.querySelector('#toast')
    const toast = document.createElement('div')

    // remover el toast automáticamente
    const autoRemoveId = setTimeout(function () {
      container.removeChild(toast)
    }, duration + 1000)

    // remover el toast cuando se pulse clic
    toast.onclick = function (e) {
      if (e.target.closest('.toast__close')) {
        //main.removeChild(toast)
        clearTimeout(autoRemoveId)
      }
    }

    const type = {
      success: icons.checkCircleFill,
      info: icons.infoCircleFill,
      warning: icons.exclamationCircleFill2,
      danger: icons.xCircleFill,
    }
    const iconType = type[mode]
    const delay = (duration / 1000).toFixed(2)

    toast.classList.add('mytoast', `toast--${mode}`)
    toast.style.animation = `slideInLeft ease .3s, fadeOut linear 1s ${delay}s forwards`

    toast.innerHTML = `
        <div class="toast__icon">
            <i class="">${iconType}</i>
        </div>
        <div class="toast__body">
            <h3 class="toast__title">${title}</h3>
            <p class="toast__msg">${message}</p>
        </div>
        <div class="toast__close">
            <i>${icons.xLg}</i>
        </div>
    `
    if (error) {
      console.error(error)
    }
    container.appendChild(toast)
  }
}
