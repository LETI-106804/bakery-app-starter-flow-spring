/**
 * ScrollShadowMixin is a mixin function for web components that adds dynamic scroll shadow handling.
 * <p>
 * When applied to a subclass, this mixin checks if there is a <code>#main</code> element in the component's shadow DOM.
 * If the content inside the <code>#main</code> element overflows vertically (i.e., the scroll height is greater than its client height),
 * and there is hidden content at the bottom, the mixin manages a <code>no-scroll</code> attribute to reflect this state.
 * This allows the component to respond visually (such as by showing or hiding a scroll shadow) depending on the scroll position.
 * </p>
 *
 * <p>
 * Usage Example:
 * <pre>
 *   class MyComponent extends ScrollShadowMixin(LitElement) {
 *     // component implementation
 *   }
 * </pre>
 * </p>
 *
 * <p>
 * The mixin defines the following reactive properties:
 * <ul>
 *   <li><code>noScroll</code> (Boolean): Reflects whether scrolling is possible or if the content is fully visible.</li>
 *   <li><code>_main</code> (Element): Reference to the <code>#main</code> element in the shadow DOM (not reflected as an attribute).</li>
 * </ul>
 * </p>
 *
 * @function ScrollShadowMixin
 * @param {Class} subclass - The class to extend.
 * @returns {Class} The extended class with scroll shadow functionality.
 */
export const ScrollShadowMixin = (subclass) =>
  /**
   * Anonymous class returned by ScrollShadowMixin. Handles scroll shadow logic based on the #main element's scroll state.
   * @class
   * @extends {subclass}
   */
  class extends subclass {
    /**
     * Defines reactive properties for the component.
     * @returns {Object} Property definitions for LitElement.
     */
    static get properties() {
      return {
        /**
         * Reflects whether the content is fully visible (no scrolling needed).
         * If true, disables the scroll shadow.
         */
        noScroll: {
          type: Boolean,
          reflect: true,
          attribute: 'no-scroll',
        },
        /**
         * Reference to the #main element. Not reflected as an attribute.
         * @type {Element}
         */
        _main: {
          attribute: false,
        },
      };
    }

    /**
     * Lifecycle callback called after the component is first updated.
     * Sets up a scroll event listener on the #main element and initializes scroll shadow state.
     */
    firstUpdated() {
      super.firstUpdated();

      this._main = this.shadowRoot.querySelector('#main');

      if (this._main) {
        this._main.addEventListener('scroll', () => this._contentScroll());
        this._contentScroll();
      }
    }

    /**
     * Updates the noScroll property based on the scroll position of the #main element.
     * Sets noScroll to true if all content is visible (scrolled to the bottom), otherwise false.
     * This can be used to show or hide scroll shadows in the UI.
     * @private
     */
    _contentScroll() {
      if (this._main) {
        this.noScroll =
          this._main.scrollHeight - this._main.scrollTop ==
          this._main.clientHeight;
      }
    }
  };
