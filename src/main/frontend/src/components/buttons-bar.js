import { html, css, LitElement } from 'lit';

/**
 * `buttons-bar`
 * 
 * A custom web component that displays a flexible bar for buttons and information.
 * It supports left, info, and right slots to arrange content across the bar.
 * 
 * ## Features:
 * - Flexible layout with space-between alignment.
 * - Info slot aligns right and stretches as needed.
 * - Responsive for small screens (info slot moves to full width).
 * - Customizable box-shadow, disables shadow with `no-scroll` attribute.
 * - Uses CSS custom properties for spacing and shadow color.
 * 
 * ## Slots:
 * - `left`: Place left-aligned content (e.g., buttons).
 * - `info`: Place informational content in the center/right.
 * - `right`: Place right-aligned content (e.g., action buttons).
 * 
 * ## Example usage:
 * ```html
 * <buttons-bar>
 *   <vaadin-button slot="left">Back</vaadin-button>
 *   <span slot="info">Some Information</span>
 *   <vaadin-button slot="right">Save</vaadin-button>
 * </buttons-bar>
 * ```
 * 
 * @element buttons-bar
 * @cssprop --lumo-space-s - Top padding and slot margin spacing.
 * @cssprop --lumo-space-xs - Button margin spacing.
 * @cssprop --lumo-shade-20pct - Box shadow color.
 * @attr no-scroll - Disables the box shadow for the bar.
 * 
 * @extends LitElement
 */
class ButtonsBarElement extends LitElement {
  /**
   * Returns the CSS styles applied to the component.
   * @returns {CSSResult}
   */
  static get styles() {
    return css`
      :host {
        flex: none;
        display: flex;
        flex-wrap: wrap;
        transition: box-shadow 0.2s;
        justify-content: space-between;
        padding-top: var(--lumo-space-s);
        align-items: baseline;
        box-shadow: 0 -3px 3px -3px var(--lumo-shade-20pct);
      }

      :host([no-scroll]) {
        box-shadow: none;
      }

      :host ::slotted([slot='info']),
      .info {
        text-align: right;
        flex: 1;
      }

      ::slotted(vaadin-button) {
        margin: var(--lumo-space-xs);
      }

      @media (max-width: 600px) {
        :host ::slotted([slot='info']) {
          order: -1;
          min-width: 100%;
          flex-basis: 100%;
        }
      }
    `;
  }

  /**
   * Renders the slots for left, info, and right content.
   * @returns {TemplateResult}
   */
  render() {
    return html`
      <slot name="left"></slot>
      <slot name="info"><div class="info"></div></slot>
      <slot name="right"></slot>
    `;
  }

  /**
   * The tag name used to define the custom element.
   * @returns {string}
   */
  static get is() {
    return 'buttons-bar';
  }
}

customElements.define(ButtonsBarElement.is, ButtonsBarElement);
